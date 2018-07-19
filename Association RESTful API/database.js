var mongoose = require('mongoose');
var debug = typeof v8debug === 'object';

mongoose.connect('mongodb://associations:associations@ds111420.mlab.com:11420/associations', function(err) {
    if (err) throw err;
});

var con = mongoose.connection;
con.on('error', function (err){
    console.log('errore di connessione', err);
});

con.once('open', function (){
   console.log('connessione riuscita!');
});