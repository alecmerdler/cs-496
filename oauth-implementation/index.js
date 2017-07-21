const express = require('express');
const path = require('path');


const PORT = process.env.PORT || 8080;
const app = express();

/**
 * Route definitions.
 */
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

// Bootstrap the application
app.listen(PORT, () => {
    console.log(`App listening on ${PORT}!`);
});
