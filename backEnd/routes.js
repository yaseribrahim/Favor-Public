const express = require('express')
const router = express.Router()
const jwt = require('jsonwebtoken');
const User = require('./models/User')
const config = require('./config')
const express_jwt = require('express-jwt')

router.get('/', (req, res) => {
  res.send(`app running on ${config.LISTEN_PORT}`)
})

// Authenticate and authorize user
router.post('/login', (req,res, next) => {

  // Assign user new token if he exists or create a new user
  User.findOne({ facebookID: req.body.facebookID}, (err, user) => {
    if (err) {
      console.log(err)
      res.json({ status: `error`})

    } else if (user) {
      console.log(`user found\n${user}`)

      //send new token
      let token =jwt.sign(user, config.TOKEN_SECRET)
      res.json({ status: `SUCCESS`, info: `user found`, user: user, token: `${token}`})

    } else{

      // create new user
      let newUser = new User()
      newUser.name = req.body.name
      newUser.facebookID = req.body.facebookID
      newUser.imageURL = `https://graph.facebook.com/${newUser.facebookID}/picture?type=large`
      newUser.dateJoined = Date.now()

      console.log(`new user created\n${newUser}`)

      User.create(newUser, (err, doc) => {
        if(err) {console.log(err)
        } else{
          console.log(`new user created\n${newUser}`)
        }

        // send user info and jsonwebtoken
        let token = jwt.sign(newUser, config.TOKEN_SECRET)
        res.send({ status: `SUCCESS`, info: `new user created`, user: doc, token: `${token}`})
      })
    }
  })
})

// TODO: refresh Jwt frequently 
module.exports = router
