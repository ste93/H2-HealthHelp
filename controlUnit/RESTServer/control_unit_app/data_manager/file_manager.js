/**
* Function to handle file operation with JSON.
*/
var fs = require('fs');

/**
* Initialize File -> Create it if not exist and initialize with an empty JSON list.
* @param filename is the name of the file to be initialized.
*/
module.exports.initialiseJsonFile = function(filename) {
    if (!fs.existsSync(filename)) {
        fs.writeFile(filename, '[]', 'utf8', function(error) {
            if(error) {
                console.log(error);
            }
        });
    }
}

/**
* Write a JSON object to the specified file.
* @param jsonElement is a JSON object ( single or list ) to be saved.
* @param filename is the name of the file to be initialized.
*/
module.exports.writeJsonToFile = function(jsonElement, filename, callback){
    var json = JSON.stringify(jsonElement);
    fs.writeFile(filename, json, 'utf8', callback);
}

/**
* Read the content of a file and return as a JSON object to the callback.
* @param filename is the name of the file to be read.
*/
module.exports.readJsonFromFile = function(filename, callback) {
    fs.readFile(filename, 'utf8', function readFileCallback(err, data){
        var jsonObject = {}
        if (err){
            console.log(err);
        } else {
            jsonObject = JSON.parse(data);
        }
        callback(err, jsonObject);
    });
}

/**
* Clear the file content and reinitialize with an empty JSON list.
* @param filename is the name of the file to be cleared.
*/
module.exports.clearFile = function(filename){
  fs.writeFile(filename, '[]', 'utf8', function(error) {
      if(error) {
          console.log(error);
      }
  });
}

/**
* Delete an existing file.
* @param filename is the name of the file to be deleted.
*/
module.exports.deleteFile = function(filename, callback) {
    fs.unlink(filename, callback);
}
