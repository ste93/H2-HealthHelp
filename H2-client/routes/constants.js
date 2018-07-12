//var amqpAddress = 'amqp://admin:exchange@213.209.230.94:8088';
var amqpAddress = 'amqp://guest:guest@192.168.43.120:5672';
var exchangeTypeConstant = 'topic'
var notificationLevelExchangeName = 'level'
var notificationLevel2QueueName = "level2.queue";
var notificationLevel3QueueName = "level3.queue";
var notificationAdviceExchangeName = 'advice';
var notificationAdviceQueueName = "advice.queue";
var adviceRequestExchangeName = 'adviceRequest';
var adviceRequestRoutingKey = 'datacentre.request.advice';
var adviceReceiveQueueName = "advice";
var advicePublishExchangeName = 'advice';
var advicePublishRoutingKey = 'datacentre.receive.advice';
var drugRequestExchangeName = 'drugRequest';
var datacentreDrugRequestRoutingKey = 'datacentre.request.drug';
var drugRequestQueueName = "drug";
var newDrugSendExchangeName = 'drug';
var newDrugSendRoutingKey = 'datacentre.receive.drug';
var datacentreHistoryRequestExchangeName = 'historyRequest';
var datacentreHistoryRequestRoutingKey = 'datacentre.request.history';
var receiveHistoryExchangeName = 'historyRequest';
var receiveHistoryQueueName = "history";
var datacentreRequestInfoExchangeName = 'info';
var datacentreRequestInfoRoutingKey = 'datacentre.request.info';
var datacentreReceiveInfoExchangeName = 'receive.info';
var datacentreReceiveInfoRoutingKey = 'datacentre.receive.info';
var datacentreReceiveInfoQueueName = "info";
var doctorRoleConstant = 'doctor';
var patientRoleConstant = 'patient'
module.exports = {  newDrugSendExchangeName,
                    newDrugSendRoutingKey,
                    notificationAdviceQueueName,
                    notificationAdviceExchangeName,
                    notificationLevel3QueueName, 
                    notificationLevel2QueueName,
                    amqpAddress, 
                    exchangeTypeConstant, 
                    notificationLevelExchangeName,
                    adviceRequestExchangeName,
                    adviceRequestRoutingKey,
                    adviceReceiveQueueName,
                    advicePublishExchangeName,
                    advicePublishRoutingKey,
                    drugRequestExchangeName,
                    datacentreDrugRequestRoutingKey,
                    drugRequestQueueName,
                    datacentreHistoryRequestExchangeName,
                    datacentreHistoryRequestRoutingKey,
                    receiveHistoryExchangeName,
                    receiveHistoryQueueName,
                    datacentreRequestInfoExchangeName,
                    datacentreRequestInfoRoutingKey,
                    datacentreReceiveInfoExchangeName,
                    datacentreReceiveInfoRoutingKey,
                    datacentreReceiveInfoQueueName,
                    doctorRoleConstant,
                    patientRoleConstant
                }