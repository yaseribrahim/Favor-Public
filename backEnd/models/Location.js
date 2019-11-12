let mongoose = require('mongoose');

let LocationSchema = new mongoose.Schema({
  socketID: String,
  coordinates: {
    latitude: Number,
    longitude: Number
  },
  locality: String

})

module.exports = mongoose.model('Location', LocationSchema);
