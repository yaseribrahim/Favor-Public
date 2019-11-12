const socketIO = require('socket.io')
const config = require('./config')
const socketioJwt   = require("socketio-jwt")
const geolib = require('geolib')
const _ = require('lodash');
var encode = require( 'hashcode' ).hashCode
const Post = require('./models/Post')
const Location = require('./models/Location')
const Message = require('./models/Message')
const User = require('./models/User')
const Chat = require('./models/Chat')
const UnreadMessageCount = require('./models/UnreadMessageCount')


module.exports = (server) => {

  // create socket and setup jwt authorization for incoming connections
  const io = socketIO(server)

  io.use(socketioJwt.authorize({
      secret: config.TOKEN_SECRET,
      handshake: true
    }))

  io.on('connection', (socket) => {
    console.log(`${socket.decoded_token._doc.name} connected`)

    // store users current socketID
    query = {'_id': socket.decoded_token._doc._id}
    User.update(query, {socketID : socket.id }, (err) => {
      if (err) {console.log(err)
      } else {
        // TODO: handle throug REST API
        loadInbox()
      }
    })


    // find all posts in locality (option is given when user not within radius of posts)
    socket.on('requestAllPosts', function(){
      var feed = []
      // find users location
      let query = {'socketID': socket.id}
      Location.findOne(query, function (err, location){
        if (err){console.log(`user ${socket.id} location not found`)
      }else {

        // find all posts within the same city
          let userLocality = location.locality
          let query = {'locality':userLocality}
          Post.find(query, function(err, posts) {
            if(err){console.log(err)
            }else{

              var postsProcessed = 0
              var postsPopulated = 0
              posts.forEach( function(post){
                let userDistance = geolib.getDistance(location.coordinates, post.coordinates)
                  postsProcessed++

                  post.populate('postedBy', function(err) {
                    postsPopulated++

                    var feedPost = new Object()
                    feedPost.postedBy = post.postedBy
                    feedPost.body = post.body
                    feedPost.hashCode = encode().value(post._id) // unique number needed for android recyclerview
                    feedPost.time = post.time
                    feedPost.price = post.price.toFixed(2)
                    feedPost.distance = userDistance

                    feed.push(feedPost)

                    if (postsPopulated === postsProcessed) {
                      feed = _.orderBy(feed, ['time'], ['desc'])

                      if (feed.length != 0){
                        socket.emit('newFeed', feed)
                      } else {
                        socket.emit('noPostsInLocality')
                      }
                    }
                  })
                })
              }
            })
          }
        })
      })

    // user posts favor
    socket.on('postMade', function (data) {
      // create new post model
      let newPost = new Post()
      newPost.postedBy = socket.decoded_token._doc._id
      newPost.body = data.message
      newPost.radius = data.radius
      newPost.price = data.price
      newPost.coordinates = data.coordinates
      newPost.locality = data.locality
      newPost.time = Date.now()

      // save to database
      Post.create(newPost, (err,doc) => {
        if(err) {console.log(err)}
        else{
          newPost.populate('postedBy', function(err) {
            if (err) {console.log(err)
            } else {

              console.log(`${newPost.postedBy.name} posted`)

              // emit to users within radius
              let query = {'locality': newPost.locality}

              Location.find(query, function(err, userLocations) {
                if (err) {console.log(err)
                } else {
                  userLocations.forEach( function(location) {
                    let userDistance = geolib.getDistance(location.coordinates, newPost.coordinates)
                    if(newPost.radius >= userDistance) {

                      let feedPost = new Object()
                      feedPost.postedBy = doc.postedBy
                      feedPost.hashCode = encode().value(doc._id) // (almost) unique number needed for android recyclerview
                      feedPost.body = doc.body
                      feedPost.time = doc.time
                      feedPost.price = doc.price.toFixed(2)
                      feedPost.distance = userDistance


                      io.to(`${location.socketID}`).emit('newPost', feedPost)
                    }
                  })
                }
              })
            }
          })
        }
      })
    })

    socket.on('locationUpdate', function(data) {

        let newLocation = {}
        newLocation.socketID = socket.id
        newLocation.coordinates = data.coordinates
        newLocation.locality = data.locality


        let query = {'socketID': socket.id}
        //TODO: something is off here. Doesn't always update/delete.
        Location.findOneAndUpdate(query, newLocation, {upsert:true, new: true}, function(err, doc){
          if (err){console.log(err)
          } else {

            loadProfile(socket.decoded_token._doc._id, doc)

            loadFeed(doc)
            // socket.emit('locationUpdated')
          }
        })
      })

    socket.on('loadChat', function(otherUserID) {

      User.findOne({'_id': otherUserID}, function(err, user){
        socket.emit('userInfo', user)
      })

      let query = {$or: [
            { $and: [{'sender': otherUserID}, {'receiver': socket.decoded_token._doc._id}] },
            { $and: [{'sender': socket.decoded_token._doc._id}, {'receiver': otherUserID}] }
        ]}
      Message.find(query).exec( function(err, messages) {
        if(err){console.log(err)
        }else {
          messages = _.orderBy(messages, ['time'], ['asc'])
          socket.emit('previousMessages', messages)
        }
      })

    })

    socket.on('sendMessage', function(data) {

      let message = new Message()
      message.receiver = data.receiver
      message.sender = socket.decoded_token._doc._id
      message.body = data.body
      message.time = Date.now()


      // update chat
      message.save(function(err, savedMessage) {

        let chatID = getChatID(savedMessage.sender, savedMessage.receiver)

        Chat.findOneAndUpdate({'chatID': chatID },
          {'latestMessage':savedMessage._id , 'participants': [savedMessage.sender, savedMessage.receiver]},
          {upsert:true, new:true})
          .populate('participants', 'name imageURL')
          .populate('latestMessage')
          .exec( function(err, chat){

            emitMessage(savedMessage, chatID)

            sendInboxItemToSender(chat, chatID)

            sendInboxItemToReceiver(chat, chatID)
        })
      })
    })


    // generate unique ID by concatenating unique ID of users
    function getChatID(sender, receiver) {

      // order matters
      if (sender > receiver) {
        return sender+receiver
      } else {
        return receiver+sender
      }
    }

    function emitMessage(message, chatID){
      // send new msg to sender
      socket.emit('newMessage', message)

      // mark message as unread
      console.log('*****message.receiver')
      console.log(message.receiver)

      // updateOne can't increment so findOneAndUpdate is used instead
      UnreadMessageCount.findOneAndUpdate({'chatID': chatID, 'userID': message.receiver},
      {$inc : {'count':1}}, {upsert:true, new:true}, function (err, doc){})

      // find receiver current socketID
      User.findOne({'_id':message.receiver}, function(err, receiver) {
        if (err) {console.log(err)
        }else{
          // only send again if user isn't chatting with himself
          if (message.receiver != socket.decoded_token._doc._id) {

            // send msg to receiver
            io.to(receiver.socketID).emit('newMessage', message)
          }
        }
      })
    }

    function sendInboxItemToSender(chat, chatID){
      // find who user is chatting with
      let receiver = socket.decoded_token._doc
      for(let participant of chat.participants) {
        if(String(participant._id) !== String(socket.decoded_token._doc._id)){
          receiver = participant
          break
        }
      }

      let inboxItem = new Object()
      inboxItem.participant = receiver
      inboxItem.latestMessage = chat.latestMessage
      inboxItem.chatID = chatID
      inboxItem.unreadMessages = 0

      // send InboxItem for sender
      socket.emit('chatUpdate', inboxItem)
    }

    function sendInboxItemToReceiver(chat, chatID) {
      // find who user is chatting with
      let receiver = socket.decoded_token._doc
      for(let participant of chat.participants) {
        if(String(participant._id) !== String(socket.decoded_token._doc._id)){
          receiver = participant
          break
        }
      }

      // find receiver current socketID
      User.findOne({'_id':receiver._id}, function(err, receiverFullDoc) {
        if (err) {console.log(err)
        }else{

          // only send again if user isn't chatting with himself
          if (receiver._id != socket.decoded_token._doc._id) {

            // get count of unread messages
            UnreadMessageCount.findOne({'chatID': chatID, 'userID': receiver._id},
             function(error, doc){
              if (err){console.log(err)}

              let inboxItem = new Object()

              inboxItem.participant = socket.decoded_token._doc // receiver views sender info
              inboxItem.latestMessage = chat.latestMessage
              inboxItem.chatID = chatID
              inboxItem.unreadMessages = doc.count;
              // send InboxItem to receiver
              io.to(receiverFullDoc.socketID).emit('chatUpdate', inboxItem)
            })
          }
        }
      })
    }

    socket.on('shareLocation', function(otherUserID) {

      Location.findOne({'socketID': socket.id}, (err, doc) =>{

        let latitude = doc.coordinates.latitude
        let longitude = doc.coordinates.longitude

        let message  = new Message()
        message.receiver = otherUserID
        message.sender = socket.decoded_token._doc._id
        message.time = Date.now()
        message.mapsURL = `https://maps.googleapis.com/maps/api/staticmap?markers=color:red%7C${latitude},${longitude}&zoom=15&size=240x240&maptype=roadmap&key=${config.STATIC_MAP_KEY}`
        message.body = '::map::'
        message.coordinates = doc.coordinates

        message.save(function() {
            socket.emit('newMessage', message)
        })
      })
    })

    function loadProfile(userID, location) {
      // get basic user info (dateJoined, rating)
      User.findOne({'_id': userID}, function(err, user) {
        let userInfo = new Object()
        // userInfo.rating = user.rating
        userInfo.name = user.name
        userInfo.imageURL = user.imageURL
        userInfo.dateJoined = user.dateJoined
        socket.emit("profileInfo", userInfo)
      })

      // get currently live posts
      Post.find({'postedBy': userID}).populate('postedBy').exec(function (err, favors) {

        // Location.findOne({'socketID': socket.id}, (err,location) => {
        //   if (err) {console.log(err)
        //   } else {
            let posts = []
            let itemsProcessed = 0

            //add distance parameter
            favors.forEach(function(favor){
              itemsProcessed++

              let userDistance = geolib.getDistance(location.coordinates, favor.coordinates)

              var feedPost = new Object()
              feedPost.postedBy = favor.postedBy
              feedPost.body = favor.body
              feedPost.hashCode = encode().value(favor._id) // unique number needed for android recyclerview
              feedPost.time = favor.time
              feedPost.price = favor.price.toFixed(2)
              feedPost.distance = userDistance

              posts.push(feedPost)

              if(itemsProcessed == favors.length) {
                posts = _.orderBy(posts, ['time'], ['desc'])
                console.log('******LIVE POSTS*****')
                console.log(posts)
                socket.emit('livePostsUpdate', posts)
              }
            })
          // }
        })
      // })
    }

    function loadInbox() {

      Chat.find({'participants':socket.decoded_token._doc._id})
        .populate('participants', 'name imageURL')
        .populate('latestMessage')
        .exec( function (err, chats){

          let inboxItems = []
          let i = 0
          // only send info of the person user is talking it to
          chats.forEach( function(chat) {
            // if user isn't talking to himself, get other user info
            let otherParticipant = socket.decoded_token._doc
            for(let participant of chat.participants) {
              if(String(participant._id) !== String(socket.decoded_token._doc._id)){
                otherParticipant = participant
                break
              }
            }
          let chatID = getChatID(socket.decoded_token._doc._id, otherParticipant._id)

          let inboxItem = new Object()
          inboxItem.participant = otherParticipant
          inboxItem.latestMessage = chat.latestMessage
          inboxItem.chatID = chatID
          // inboxItem._id = chat._id
          // get count of unread messages
          UnreadMessageCount.findOne({'chatID': chatID, 'userID': socket.decoded_token._doc._id},
           function(error, doc){
            if (err){console.log(err)}

            if(doc){
            inboxItem.unreadMessages = doc.count;
            } else {
              inboxItem.unreadMessages = 0;
            }

            // then push chat into inbox
            inboxItems.push(inboxItem)
            i++

            if (i == chats.length) {
              inboxItems = _.orderBy(inboxItems, ['latestMessage.time'], ['desc'])
              socket.emit('chats', inboxItems)
            }
          })
        })
      })
    }

    function loadFeed(location) {

      var feed = []
      // // find users location
      // let query = {'socketID': socket.id}
      // Location.findOne(query, function (err, location){
      //   if (err){console.log(`user ${socket.id} location not found`)}

        // find all posts within the same city
        let userLocality = location.locality
        let query = {'locality':userLocality}
        Post.find(query).populate('postedBy').exec(function(err, posts) {
          if(err){console.log(err)
          }else{

            // if user is within radius of posts, add post to feed array
            let postsWithinDistance = 0
            let postsPopulated = 0
            let postsProcessed = 0
            posts.forEach( function(post){
              postsProcessed++

              let userDistance = geolib.getDistance(location.coordinates, post.coordinates)

              if (post.radius >= userDistance) {
                postsWithinDistance++

                var feedPost = new Object()
                feedPost.postedBy = post.postedBy
                feedPost.body = post.body
                feedPost.hashCode = encode().value(post._id) // unique number needed for android recyclerview
                feedPost.time = post.time
                feedPost.price = post.price.toFixed(2)
                feedPost.distance = userDistance

                feed.push(feedPost)
              }
            })
            feed = _.orderBy(feed, ['time'], ['desc'])
            // console.log(feed)
            socket.emit('newFeed', feed)
          }
        })
      // })
    }


    socket.on('markChatAsRead', function(participant) {

      let chatID = getChatID(socket.decoded_token._doc._id, participant.participantID)

      UnreadMessageCount.findOneAndUpdate({'chatID': chatID, 'userID': socket.decoded_token._doc._id},
      {'count':0}, {upsert:true, new:true}, function (err, doc){
        console.log('***doc***\n' + doc)

        Chat.findOne({'chatID':chatID})
          .populate('participants', 'name imageURL')
          .populate('latestMessage')
          .exec( function(err, chat){
          console.log('***chat***\n' + chat)

          if(chat){
          sendInboxItemToSender(chat, chatID)
          }
        })
      })
    })

    socket.on('requestPossibleHelpers', function () {
      // find people user communicated with
      Message.find({'receiver': socket.decoded_token._doc._id})
        .distinct('sender', function (err, users) {
          User.find({'_id':{$in: users}}, function(error, helpers){
            socket.emit("possibleHelpers", helpers)
        })
      })
    })

    socket.on('disconnect', function() {
      let query = {'socketID': socket.id}
      console.log(socket.id)
      Location.deleteMany(query, function(err) {
        if(err) { console.log(err)}
        console.log(`location info deleted for ${socket.id}`)
      })
      console.log(`${socket.decoded_token._doc.name} disconnected`)
    })

  })
}
