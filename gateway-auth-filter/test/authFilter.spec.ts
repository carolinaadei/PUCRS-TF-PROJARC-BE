import { AuthFilter } from '../src/gateway/authFilter';

describe('AuthFilter', () => {
    let authFilter: AuthFilter;

    beforeEach(() => {
        authFilter = new AuthFilter();
    });

    describe('validateToken', () => {
        it('should return true for a valid token', () => {
            const validToken = 'valid.jwt.token';
            const result = authFilter.validateToken(validToken);
            expect(result).toBe(true);
        });

        it('should return false for an invalid token', () => {
            const invalidToken = 'invalid.jwt.token';
            const result = authFilter.validateToken(invalidToken);
            expect(result).toBe(false);
        });
    });

    describe('handleRequest', () => {
        it('should call next() if token is valid', () => {
            const req = { headers: { authorization: 'Bearer valid.jwt.token' } };
            const next = jest.fn();
            authFilter.handleRequest(req, {}, next);
            expect(next).toHaveBeenCalled();
        });

        it('should return 401 if token is invalid', () => {
            const req = { headers: { authorization: 'Bearer invalid.jwt.token' } };
            const res = { status: jest.fn().mockReturnThis(), send: jest.fn() };
            authFilter.handleRequest(req, res, () => {});
            expect(res.status).toHaveBeenCalledWith(401);
            expect(res.send).toHaveBeenCalledWith({ message: 'Unauthorized' });
        });
    });
});