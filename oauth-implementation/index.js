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

let oauthTokenData = null;

const newOAuthToken = (tokenData) => {
    if (tokenData != undefined) {
        console.log(`New OAuth token will expire in ${tokenData.expires_in} seconds`);
        // Destroy the token once it expires
        setTimeout(() => {
            console.log(`Destroying OAuth token`);
            oauthTokenData = null;
        }, tokenData.expires_in * 1000);

        return Object.assign({}, tokenData);
    } else {
        return null;
    }
};

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
            client_secret: `HmuXTvPXtyuxNBWwgGpKpKoL`,
            redirect_uri: `https://${req.headers['host']}/oauth`,
            grant_type: `authorization_code`,
        };
        const options = {
            method: 'POST',
            uri: `https://www.googleapis.com/oauth2/v4/token`,
            form: requestBody,
        };
        request(options, (error, response, body) => {
            const json = JSON.parse(body);

            oauthTokenData = newOAuthToken(json);

            res.sendFile(path.join(__dirname, 'oauth.html'));
        });
    } else {
        res.status(400)
           .json({'error': "missing field 'state'"});
    }
});

app.get('/me', (req, res) => {
    if (oauthTokenData != undefined) {
        const options = {
            method: 'GET',
            uri: `https://www.googleapis.com/plus/v1/people/me`,
            qs: {access_token: oauthTokenData.access_token}
        };
        request(options, (error, response, body) => {
            console.log(body);

            res.json(body);
        });
    } else {
        res.status(401)
           .json({'error': "please authenticate using OAuth 2.0 endpoint"})
    }
});

// Bootstrap the application
http.createServer(app).listen(8080);
https.createServer(credentials, app).listen(8443);
