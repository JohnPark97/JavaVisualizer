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

const formatData = (data) => {
  let classes = []
  let links = [];
  data.classes.forEach((klass) => {
    Object.entries(klass).forEach((prop) => {
      classes.push({
        className: prop[0],
        lineCount: prop[1],
      });
    });
  });

  data.links.forEach((link) => {
    Object.entries(link).forEach((relationship) => {
      relationship[1].forEach((target) => {
        links.push({
          source: relationship[0],
          target,
        });
      });
    });
  });
  console.log(classes)
  console.log(links)

  return { classes, links };
}