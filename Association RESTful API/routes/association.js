var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

 /** Connect Mongo DB */
require('../database');
var db = mongoose.connection;

/* Model Scheme */
var doctors = require('../models/doctor');
var patients = require('../models/patient');
var associations = require('../models/patientDoctor');

/* GET home page. */
router.get('/', function(req, res, next) {
});

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

/*  --- GET, POST, DELETE for doctors*/
router.get('/doctors', function(req, res, next){

    var id = req.param('_id');
    
   doctors.findOne({"_id": id },function(err, doc) {
      if (err) {
          console.error(err);
          return ;
      }
      res.json(doc); 
    });
  
  
});
  

router.post('/doctors', function(req,res,next){
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
      
      doctors.create(patient, function(err, pat) {
          if (err) console.log('Error created patient - %s', err);
         res.send(pat.insertedId);
       });
  });
  
  router.delete('/doctors', function(req, res, next){
      var id = req.param('_id');
  
     doctors.remove({"_id": id}, function(err, ass){
          if (err) console.log('Error delete patient - %s', err);
          
          associations.remove({"idDoctor":id}, function(err, ass){
            console.log('removed the dependecy')
          });
          
          res.send(ass)
      });

      
  });


  /* associations API  */

router.post('/relationship', function(req, res, next){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');

    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };
    
    db.collection('relationship').insert(association, function(err, doc) {
        if (err) console.log('Error created association - %s', err);
       res.send(doc.insertedId);
     });
});

router.get('/relationship', function(req, res, next){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');
    
    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };

    if(idPatient == undefined){
        associations.find({"idDoctor": idDoctor},{"_id":0, "idPatient":1},function(err, doc) {
            if (err) console.log(' Association Error - %s', err);
            
          res.json(doc);       
        });  
    }else if(idDoctor == undefined){
        associations.find({"idPatient": idPatient},{"_id":0, "idDoctor":1},function(err, doc) {
            if (err) console.log(' Association Error - %s', err);
          res.json(doc);
        });
    }else{
        associations.findOne(association,{"_id":0}, function(err, doc) {
            if (err) console.log(' Associaton not exist - %s', err);
            res.json(doc);
        });
    }
});

router.delete('/relationship', function(req, res, next){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');
    
    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };
    associations.remove(association, {"_id":0},function(err, doc) {
        if (err) console.log(' Associaton not exist - %s', err);
        res.json(doc);
     });
});

module.exports = router;