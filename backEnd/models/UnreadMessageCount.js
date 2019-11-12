let mongoose = require('mongoose')

let UnreadMessageCountSchema = new mongoose.Schema({
  userID: String,
  chatID: String,
  count: Number
})

module.exports = mongoose.model('UnreadMessageCount', UnreadMessageCountSchema);
