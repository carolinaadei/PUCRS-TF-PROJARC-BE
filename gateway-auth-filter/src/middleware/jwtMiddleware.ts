import { Request, Response, NextFunction } from 'express';
import { AuthFilter } from '../gateway/authFilter';

export const jwtMiddleware = (req: Request, res: Response, next: NextFunction) => {
    const token = req.headers['authorization']?.split(' ')[1];

    if (!token) {
        return res.status(401).json({ message: 'No token provided' });
    }

    const authFilter = new AuthFilter();
    const isValid = authFilter.validateToken(token);

    if (!isValid) {
        return res.status(403).json({ message: 'Invalid token' });
    }

    next();
};