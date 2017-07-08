const https = require('https');
const assert = require('assert');


const githubURL = 'api.github.com';
const username = 'alecmerdler';
const oauthToken = process.env.GITHUB_OAUTH_TOKEN;


/**
 * Execute a request against the Gists API.
 * @param endpoint The resource path.
 * @param method Which HTTP method to use.
 * @param requestData An object containing POST/PUT data.
 * @returns response A promise that resolves to the response data.
 */
const makeRequest = (endpoint, method, requestData) => {
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


/**
 * Remove any created Gists from the tests. Created Gists contain 'file1.txt'.
 */
async function cleanup() {
    let response = await makeRequest('/gists', 'GET');
    response.data
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

    assert(response.data.length == 30,
           `Getting public Gists returns 30 Gists.`);
}
/**
 * Confirm that the user `wolfordj` has at least one public Gist.
 */
async function testInstructorPublicGistsLength() {
    const response = await makeRequest('/users/wolfordj/gists', 'GET');

    assert(response.data.length >= 1,
           `Confirm that the user 'wolfordj' has at least one public Gist.`);
}
/**
 * Confirm that when you create a Gist the number of Gists associated to your account increases by 1.
 */
async function testCreateGistIncreasesCount() {
    let response = await makeRequest('/gists', 'GET');
    const initialGists = response.data.length;
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
    assert(response.data.length >= initialGists + 1,
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

    assert(response.data['files']['file1.txt'].content === postData.files['file1.txt'].content,
           `Confirm that the contents of the Gist you created match the contents you sent.`);
}
/**
 * Confirm that you are able to edit the contents of a Gist.
 */
async function testEditGistMatchesContent() {
    let response = await makeRequest('/gists', 'GET');
    const editedGistId = response.data.filter(gist => Object.keys(gist.files).indexOf('file1.txt') != -1)[0].id;

    const patchData = {
        files: {
            "file1.txt": {
                content: "Edited file contents",
            }
        }
    };
    response = await makeRequest(`/gists/${editedGistId}`, 'PATCH', patchData);

    assert(response.data.files['file1.txt'].content === patchData.files['file1.txt'].content,
           `Confirm that you are able to edit the contents of a Gist.`);
}
/**
 * Confirm that you can add a star to a Gist.
 */
async function testAddStarGist() {
    let response = await makeRequest('/gists', 'GET');
    await makeRequest(`/gists/${response.data[0].id}/star`, 'PUT');
    response = await makeRequest(`/gists/${response.data[0].id}/star`, 'GET');

    assert(response.statusCode == 204,
           `Confirm that you can add a star to a Gist.`);
}
/**
 * Confirm that your list of Starred gists is correct.
 */
async function testStarredGistsList() {
    let response = await makeRequest('/gists/starred', 'GET');
    const starredGists = response.data;
    Promise.all(starredGists.map(gist => makeRequest(`/gists/${gist.id}/star`)))
        .then((responses) => {
            responses.forEach((response) => {
                assert(response.statusCode == 204,
                      `Confirm that your list of Starred gists is correct.`);
            });
        });
}
/**
 * Confirm you can remove a star from a Gist.
 */
async function testUnstarGist() {
    let response = await makeRequest('/gists', 'GET');
    const unstarredGistId = response.data.filter(gist => Object.keys(gist.files).indexOf('file1.txt') != -1)[1].id;
    await makeRequest(`/gists/${unstarredGistId}/star`, 'DELETE');
    response = await makeRequest(`/gists/${unstarredGistId}/star`, 'GET');

    assert(response.statusCode === 404,
           `Confirm you can remove a star from a Gist.`);
}


// Run all the tests
Promise.all([
    testPublicGistsLength(),
    testInstructorPublicGistsLength(),
    testCreateGistIncreasesCount(),
    testCreateGistMatchesContent(),
    testEditGistMatchesContent(),
    testAddStarGist(),
    testStarredGistsList(),
    testUnstarGist(),
])
.then(() => {
    console.log(`All tests passed!`);
    cleanup();
})
.catch((error) => {
    console.error(error);
});
