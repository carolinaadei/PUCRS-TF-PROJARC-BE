import express from 'express';
import { json } from 'body-parser';
import { jwtMiddleware } from './middleware/jwtMiddleware';
import { AuthFilter } from './gateway/authFilter';
import { Proxy } from './gateway/proxy';

const app = express();
const port = process.env.PORT || 3000;

app.use(json());
app.use(jwtMiddleware);

const authFilter = new AuthFilter();
const proxy = new Proxy();

app.use((req, res, next) => {
    if (!authFilter.validateToken(req)) {
        return res.status(401).send('Unauthorized');
    }
    next();
});

app.all('*', (req, res) => {
    proxy.forwardRequest(req, res);
});

app.listen(port, () => {
    console.log(`Gateway is running on http://localhost:${port}`);
});