var mongoose = require('mongoose');
var debug = typeof v8debug === 'object';

mongoose.connect('mongodb://h2db:h2db@ds111420.mlab.com:11420/h2db', function(err) {
    if (err) throw err;
});

var con = mongoose.connection;
con.on('error', function (err){
    console.log('errore di connessione', err);
});

con.once('open', function (){
   console.log('connessione riuscita!');
});