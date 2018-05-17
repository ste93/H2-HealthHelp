var mongoose = require('mongoose');

var router = require('./association');

/*  --- GET, POST, DELETE for patients*/
router.get('/patients', function(req, res, next){
    var id = req.param('_id');
    
    patients.findOne({"_id": id },function(err, doc) {
      if (err) {
          console.error(err);
          return ;
      }
      res.json(doc); 
    });
  
  
  });
  
  router.post('/patients', function(req,res,next){
      var id = req.param('_id');
      var name = req.param('name');
      var surname = req.param('surname');
      var cf = req.param('cf');
  
      var patient = { 
          "_id" : id,
          "name" : name,
          "surname":surname,
          "cf" : cf
      };
      
      patients.create(patient, function(err, pat) {
          if (err) console.log('Error created patient - %s', err);
         res.send(pat.insertedId);
       });
  });
  
  router.delete('/patients', function(req, res, next){
      var id = req.param('_id');
  
      patients.remove({"_id": id}, function(err, pat){
          if (err) console.log('Error delete patient - %s', err);
          
          associations.remove({"idPatient":id}, function(err, ass){
              console.log('removed the dependecy')
           });
          
          res.send(pat)
      });
  });
  
  module.exports = this