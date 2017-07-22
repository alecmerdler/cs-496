const express = require('express');
const request = require('request');
const path = require('path');
const http = require('http');
const https = require('https');
const fs = require('fs');


const PORT = process.env.PORT || 8080;
const credentials = {
    key: fs.readFileSync('certs/localhost.key', 'utf-8'),
    cert: fs.readFileSync('certs/localhost.crt', 'utf-8'),
    ciphers: 'ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES256-SHA384',
    honorCipherOrder: true,
    secureProtocol: 'TLSv1_2_method'
};
const oauthState = "secret123";
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

    console.log(state, code);

    if (state === oauthState) {
        const requestBody = {
            code: code,
            client_id: `220231227847-fabrd2a3ogrstjha4ugl60626qp76vmb.apps.googleusercontent.com`,
            client_secret: `HmuXTvPXtyuxNBWwgGpKpKoL`,
            redirect_uri: `https://${req.headers['host']}/oauth`,
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
http.createServer(app).listen(8080);
https.createServer(credentials, app).listen(8443);
