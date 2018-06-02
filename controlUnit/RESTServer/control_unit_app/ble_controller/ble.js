var pub_sub = require('../pub_sub/pub_sub_test')
var nearList = [{"id" : "123"}, {"id" : "234"}, {"id" : "345"}, {"id" : "456"}]

module.exports.getNearList = function() {
  return nearList;
}


module.exports.doSomethingWith = function(id) {
  console.log("doing something with " + id + " sensors !");
  pub_sub.connectToTopic();
  //pub_sub.publishMessage("Test pub sub message");
}
