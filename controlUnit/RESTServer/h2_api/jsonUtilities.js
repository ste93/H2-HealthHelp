
module.exports.sendJsonResponse = function(res, status, content) {
  res.status(status);
  res.json(content);
};
