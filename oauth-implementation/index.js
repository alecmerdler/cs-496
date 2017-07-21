const express = require('express');
const request = require('request');
const path = require('path');


const PORT = process.env.PORT || 8080;
const oauthState = "secret";
const app = express();

/**
 * Route definitions.
 */
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.get('/oauth', (req, res) => {
    const state = req.query['state'];
    const code = req.query['code'];

    if (state === oauthState) {
        const requestBody = {
            code: code,
            client_id: `220231227847-fabrd2a3ogrstjha4ugl60626qp76vmb.apps.googleusercontent.com`,
            client_secret: `3N6K3HsSymdDfcaNWmr_p4fV`,
            redirect_uri: ``,
            grant_type: `authorization_code`,
        };
        request.post(`https://www.googleapis.com/oauth2/v4/token`, requestBody, (error, response, body) => {
            console.log(body);

            res.sendFile(path.join(__dirname, 'oauth.html'));
        });
    } else {
        res.status(400)
           .json({'error': "missing field 'state'"});
    }
});

// Bootstrap the application
app.listen(PORT, () => {
    console.log(`App listening on ${PORT}!`);
});
