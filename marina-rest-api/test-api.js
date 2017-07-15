const http = require('http');
const assert = require('assert');


/**
 * Execute a request against the API.
 * @param endpoint The resource path.
 * @param method Which HTTP method to use.
 * @param requestData An object containing POST/PUT data.
 * @returns response A promise that resolves to the response data.
 */
const makeRequest = (endpoint, method, requestData) => {
    return new Promise((resolve, reject) => {
        const request = http.request({
            host: 'localhost',
            port: 8000,
            path: `/api${endpoint}`,
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            }
        }, (response) => {
            let body = '';

            response.on('data', (data) => {
                body += data;
            });

            response.on('end', () => {
                const data = body ? JSON.parse(body) : null;
                let result = {data: data, statusCode: response.statusCode};

                resolve(result);
            });
        });

        if (requestData) {
            request.write(JSON.stringify(requestData));
        }
        request.end();
    });
};


async function testListBoats() {
    const response = await makeRequest('/boats/', 'GET');

    assert(response.data.length > 0,
           `List boats`);
}

async function testCreateBoat() {
    const initialLength = await makeRequest('/boats/', 'GET').data.length;
    const postData = {
        type: "cruiseliner",
        name: "Titanic",
        length: 500,
        at_sea: true,
    };

    await makeRequest('/boats/', 'POST', postData);
    const response = await makeRequest('/boats/', 'GET');

    assert(response.data.length > initialLength);
}


// Run all the tests
[
    testListBoats(),
    // testCreateBoat(),
].reduce((test, next) => {
        console.log(`.`);

        return test.then(next);
    },
    Promise.resolve())
    .then(() => {
        console.log(`All tests passed!`);
    });
