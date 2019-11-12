let mongoose = require('mongoose');

let PostSchema = new mongoose.Schema({
  postedBy: {type: mongoose.Types.ObjectId, ref: 'User'}, // user doc ID
  body: String,
  radius: Number,
  price: Number,
  time: Date,
  coordinates: {
    latitude: Number,
    longitude: Number
  },
  locality: String
})

// compile schema into module then export
module.exports = mongoose.model('Post', PostSchema);
  
