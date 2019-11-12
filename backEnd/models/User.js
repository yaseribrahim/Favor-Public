let mongoose = require('mongoose');

let UserSchema = new mongoose.Schema({
  name: String,
  socketID: String,
  facebookID: String,
  imageURL: String,
  rating: Number,
  dateJoined: Date
})

module.exports = mongoose.model('User', UserSchema);
