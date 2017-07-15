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
        const requestJSON = JSON.stringify(requestData);

        const request = http.request({
            host: 'localhost',
            port: 8000,
            path: `/api${endpoint}`,
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Content-Length': requestJSON ? Buffer.byteLength(requestJSON) : 0,
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
            request.write(requestJSON);
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
    const boatID = response.data[0].id;
    response = await makeRequest(`/boats/${boatID}/`, 'GET');

    assert(response.data.id === boatID,
           `Retrieve boat`);
}

async function testCreateBoat() {
    const postData = {
        type: "cruiseliner",
        name: "Titanic",
        length: 500,
    };

    var response = await makeRequest('/boats/', 'POST', postData);
    const boatId = response.data.id;
    response = await makeRequest('/boats/', 'GET');

    assert(response.data.find(boat => boat.id === boatId) != undefined,
           `Create boat`);
}

async function testUpdateBoat() {
    var response = await makeRequest('/boats/', 'GET');
    const boat = response.data[0];
    const updateData = Object.assign({}, boat, {length: boat.length + 10});

    response = await makeRequest(`/boats/${boat.id}/`, 'PUT', updateData);

    assert(response.data.length === updateData.length,
           `Update boat`);
}

async function testDeleteBoat() {
    var response = await makeRequest('/boats/', 'GET');
    const boatId = response.data[0].id;
    await makeRequest(`/boats/${boatId}/`, 'DELETE');
    response = await makeRequest(`/boats/`, 'GET');

    assert(response.data.find(boat => boat.id === boatId) == undefined,
           `Delete boat`);
}

async function testSetSailBoat() {
    var response = await makeRequest('/boats/', 'GET');
    const boat = response.data[0];
    await makeRequest(`/boats/${boat.id}/set_sail/`, 'POST');
    response = await makeRequest(`/boats/${boat.id}/`, 'GET');

    assert(response.data.at_sea == true,
           `Set sail boat`);
}

async function testDockBoat() {
    var response = await makeRequest('/boats/', 'GET');
    const boat = response.data[0];
    var response = await makeRequest('/slips/', 'GET');
    const slip = response.data[0];
    const dockData = {slip_number: slip.number};

    await makeRequest(`/boats/${boat.id}/dock/`, 'POST', dockData);
    response = await makeRequest(`/boats/${boat.id}/`, 'GET');

    assert(response.data.at_sea == false,
           `Dock boat`);
}

async function testListSlips() {
    const response = await makeRequest('/slips/', 'GET');

    assert(response.data.length > 0,
           `List slips`);
}

async function testRetrieveSlip() {
    var response = await makeRequest('/slips/', 'GET');
    const slipID = response.data[0].id;
    response = await makeRequest(`/slips/${slipID}/`, 'GET');

    assert(response.data.id === slipID,
           `Retrieve slip`);
}

async function testCreateSlip() {
    const slipData = {
        number: 10,
        arrival_date: "3/13/2007",
    };

    var response = await makeRequest('/slips/', 'POST', slipData);
    const slipID = response.data.id;
    response = await makeRequest('/slips/', 'GET');

    assert(response.data.find(slip => slip.id === slipID) != undefined,
           `Create slip`);
}

async function testUpdateSlip() {
    var response = await makeRequest('/slips/', 'GET');
    const slip = response.data[0];
    const updateData = Object.assign({}, slip, {number: slip.number + 10});

    response = await makeRequest(`/slips/${slip.id}/`, 'PUT', updateData);

    assert(response.data.number === updateData.number,
           `Update slip`);
}

async function testDeleteSlip() {
    var response = await makeRequest('/slips/', 'GET');
    const slipID = response.data[0].id;
    await makeRequest(`/slips/${slipID}/`, 'DELETE');
    response = await makeRequest(`/slips/`, 'GET');

    assert(response.data.find(slip => slip.id === slipID) == undefined,
           `Delete slip`);
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
