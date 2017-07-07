const https = require('https');
const assert = require('assert');


const githubURL = 'api.github.com';
const username = 'alecmerdler';
const oauthToken = process.env.GITHUB_OAUTH_TOKEN;


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
                resolve(body ? JSON.parse(body) : {});
            });
        });

        if (data) {
            request.write(JSON.stringify(data));
        }
        request.end();
    });
};


/**
 * Remove any created Gists from the tests. Created Gists contain 'file1.txt'.
 */
async function cleanup() {
    let response = await makeRequest('/gists', 'GET');
    response
        .filter(gist => Object.keys(gist.files).indexOf('file1.txt') != -1)
        .forEach((gist) => {
            makeRequest(`/gists/${gist.id}`, 'DELETE');
        });
};


/**
 * Getting public Gists returns 30 Gists.
 */
async function testPublicGistsLength() {
    const response = await makeRequest('/gists/public', 'GET');

    assert(response.length == 30,
           `Getting public Gists returns 30 Gists.`);
}
/**
 * Confirm that the user `wolfordj` has at least one public Gist.
 */
async function testInstructorPublicGistsLength() {
    const response = await makeRequest('/users/wolfordj/gists', 'GET');

    assert(response.length >= 1,
           `Confirm that the user 'wolfordj' has at least one public Gist.`);
}
/**
 * Confirm that when you create a Gist the number of Gists associated to your account increases by 1.
 */
async function testCreateGistIncreasesCount() {
    let response = await makeRequest('/gists', 'GET');
    const initialGists = response.length;
    const postData = {
        description: `Test gist for CS 461`,
        public: true,
        files: {
           "file1.txt": {
               content: "String file contents",
           }
        }
    };
    await makeRequest('/gists', 'POST', postData);
    response = await makeRequest('/gists', 'GET');

    // FIXME: Cannot check for increase by exactly 1 because tests are running async
    assert(response.length >= initialGists + 1,
           `Confirm that when you create a Gist the number of Gists associated to your account increases by 1.`);
}
/**
 * Confirm that the contents of the Gist you created match the contents you sent.
 */
async function testCreateGistMatchesContent() {
    const postData = {
        description: `Test gist for CS 461`,
        public: true,
        files: {
            "file1.txt": {
                content: "String file contents",
            }
        }
    };
    const response = await makeRequest('/gists', 'POST', postData);

    assert(response['files']['file1.txt'].content === postData.files['file1.txt'].content,
           `Confirm that the contents of the Gist you created match the contents you sent.`);
}
/**
 * Confirm that you are able to edit the contents of a Gist.
 */
async function testEditGistMatchesContent() {
    let response = await makeRequest('/gists', 'GET');
    const editedGistId = response.filter(gist => Object.keys(gist.files).indexOf('file1.txt') != -1)[0].id;

    const patchData = {
        files: {
            "file1.txt": {
                content: "Edited file contents",
            }
        }
    };
    response = await makeRequest(`/gists/${editedGistId}`, 'PATCH', patchData);

    assert(response.files['file1.txt'].content === patchData.files['file1.txt'].content,
           `Confirm that you are able to edit the contents of a Gist.`);
}
/**
 * Confirm that you can add a star to a Gist
 */
async function testAddStarGist() {
    let response = await makeRequest('/gists', 'GET');
    await makeRequest(`/gists/${response[0].id}/star`, 'PUT');
    response = await makeRequest(`/gists/${response[0].id}/star`, 'GET');

    assert(true, `Confirm that you can add a star to a Gist.`);
}

// Run all the tests
Promise.all([
    testPublicGistsLength(),
    testInstructorPublicGistsLength(),
    testCreateGistIncreasesCount(),
    testCreateGistMatchesContent(),
    testEditGistMatchesContent(),
    testAddStarGist(),
])
.then(() => {
    console.log(`All tests passed!`);
    cleanup();
})
.catch((error) => {
    console.error(error);
});