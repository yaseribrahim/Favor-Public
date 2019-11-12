let mongoose = require('mongoose');

let ChatSchema = new mongoose.Schema({
  chatID: String,
  participants: [{type: mongoose.Schema.Types.ObjectId, ref: 'User'}],
  latestMessage: {type: mongoose.Types.ObjectId, ref: 'Message'},
  unreadMessages: Number
})

module.exports = mongoose.model('Chat', ChatSchema)
