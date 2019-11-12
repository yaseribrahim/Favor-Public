let mongoose = require('mongoose');

let MessageSchema = new mongoose.Schema({
  sender: {type: mongoose.Types.ObjectId, ref: 'User'},
  receiver: {type: mongoose.Types.ObjectId, ref: 'User'},
  body: String,
  mapsURL: String,
  coordinates: {
    latitude: Number,
    longitude: Number
  },
  time: Date
})

// compile schema into model then export. Every instance of model is a document
module.exports = mongoose.model('Message', MessageSchema);
