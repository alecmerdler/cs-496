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

async function testRetrieveBoat() {
    var response = await makeRequest('/boats/', 'GET');
    const boatId = response.data[0].id;

    response = await makeRequest(`/boats/${boatId}/`, 'GET');

    assert(response.data.id == boatId);
}

async function testCreateBoat() {
    var response = await makeRequest('/boats/', 'GET');

    const postData = {
        type: "cruiseliner",
        name: "Titanic",
        length: 500,
    };

    response = await makeRequest('/boats/', 'POST', postData);
    console.log(response);
    const createdBoat = response.data;
    response = await makeRequest('/boats/', 'GET');
    const boats = response.data;

    assert(boats.find(boat => boat.id === createdBoat.id) != undefined);
}

async function testUpdateBoat() {
    var response = await makeRequest('/boats/', 'GET');
    const initialBoat = response.data[0];

    const updateData = {
        length: initialBoat.length + 10,
    };

    await makeRequest(`/boats/${initialBoat.id}/`, 'PUT', updateData);
    response = makeRequest(`/boats/${initialBoat.id}/`, 'GET');

    assert(response.data.length == initialBoat.length + 10);
}

async function testDeleteBoat() {

}

async function testSetSailBoat() {

}

async function testDockBoat() {

}

async function testListSlips() {

}

async function testRetrieveSlip() {

}

async function testCreateSlip() {

}

async function testUpdateSlip() {

}

async function testDeleteSlip() {

}


// Run all the tests
[
    testListBoats(),
    testRetrieveBoat(),
    testCreateBoat(),
    testUpdateBoat(),
    testDeleteBoat(),
    testSetSailBoat(),
    testDockBoat(),
    testListSlips(),
    testRetrieveSlip(),
    testCreateSlip(),
    testUpdateSlip(),
    testDeleteSlip(),
].reduce((test, next) => {
        console.log(`.`);

        return test.then(next);
    },
    Promise.resolve())
    .then(() => {
        console.log(`All tests passed!`);
    });
