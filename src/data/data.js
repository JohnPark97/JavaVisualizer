const host = 'https://api.github.com';
// const repoOwnerEndpoint = '/repos/{owner}/{repo}';
const repoOwnerEndpoint = '/repos/dwmkerr/spaceinvaders'; // as an example

const backendHost = 'http://localhost:8000'
const testEndpoint = '/data';

const getData = async (repoURL) => {
  const url = backendHost + testEndpoint;
  // TODO use the repoURL somehow
  console.log(url);

  const res = await httpRequest(url, {
    method: 'GET',
  });

  return res;
};