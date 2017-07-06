const https = require('https');
const assert = require('assert');


const githubURL = 'api.github.com';
const username = 'alecmerdler';
const oauthToken = '212d8bdb7e34038f360e1324ae3c9d34948c04d3';


/**
 * Execute a request against the Gists API.
 * @param endpoint The resource path.
 * @param method Which HTTP method to use.
 * @param data An object containing POST/PUT data.
 * @returns response A promise that resolves to the response data.
 */
const makeRequest = (endpoint, method, data) => {
    return new Promise((resolve, reject) => {
        const request = https.request({
            hostname: githubURL,
            path: endpoint,
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'User-Agent': username,
                'Authorization': `Bearer ${oauthToken}`,
            }
        }, (response) => {
            let body = '';

            response.on('data', (data) => {
                body += data;
            });

            response.on('end', () => {
                resolve(JSON.parse(body));
            });
        });

        if (data) {
            request.write(JSON.stringify(data));
        }
        request.end();
    });
};


/**
 * Getting public Gists returns 30 Gists.
 */
async function testPublicGistsLength() {
    const response = await makeRequest('/gists/public', 'GET');
    assert(response.length == 30);
}
/**
 * Confirm that the user `wolfordj` has at least one public Gist.
 */
async function testInstructorPublicGistsLength() {
    const response = await makeRequest('/users/wolfordj/gists', 'GET');
    assert(response.length >= 1);
}


// Run all the tests
testPublicGistsLength();
testInstructorPublicGistsLength();
