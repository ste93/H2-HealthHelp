//for managing file system
var fs = require('fs');

module.exports.initialiseJsonFile = function(filename) {
    if (!fs.existsSync(filename)) {
        fs.writeFile(filename, '[]', 'utf8', function(error) {
            if(error) {
                console.log(error);
            }
        });
    }
}

module.exports.writeJsonToFile = function(list, filename, callback){
    var json = JSON.stringify(list);
    fs.writeFile(filename, json, 'utf8', callback);
}

module.exports.readJsonFromFile = function(filename, callback) {
    fs.readFile(filename, 'utf8', function readFileCallback(err, data){
        var obj = {}
        if (err){
            console.log(err);
        } else {
            obj = JSON.parse(data); //now it an object
        }
        callback(err, obj);
    });
}

module.exports.deleteFile = function(filename, callback) {
    fs.unlink(filename, callback);
}
