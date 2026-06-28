export default {
  jwtSecret: process.env.JWT_SECRET || 'your-default-jwt-secret',
  jwtExpiration: process.env.JWT_EXPIRATION || '1h',
  serviceUrl: process.env.SERVICE_URL || 'http://localhost:3000',
  logLevel: process.env.LOG_LEVEL || 'info',
};