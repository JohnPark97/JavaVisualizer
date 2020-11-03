// const repoOwnerEndpoint = '/repos/{owner}/{repo}';
const repoOwnerEndpoint = '/repos/dwmkerr/spaceinvaders';
const URLHead = 'https://api.github.com';

const getData = async (repoURL) => {
  const url = URLHead + repoOwnerEndpoint;
  // TODO use the repoURL somehow
  console.log(url);
  
  const res = await httpRequest(url, {
    method: 'GET',
  });

  console.log(res);
  return res;
};