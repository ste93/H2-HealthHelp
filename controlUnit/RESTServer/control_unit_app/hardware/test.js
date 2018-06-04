var emergencyManager = require('./emergencyManager');

emergencyManager.initializeEmergencyProtocol();

setTimeout(5000, emergencyManager.startEmergency());
