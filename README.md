## Intracom full stack containerized software development
---
This repository consists of two folders
- Backend
    This folder contains the code for a simple REST api written in Java using Spark.
    It acts as a Read operation on a small test dataset (sqlite database), and simply returns the data in a JSON format.
- Frontend 
    Simple frontend written in javascript utilizing jQuery to render a simple table view of the returned data

### Notes
The current configuration assumes that the stack will be deployed to Okteto cloud, with the docker-compose.yaml file. I configured it this way for 2 reasons:
1. My poor laptop couldnt handle all the building and running of the containers.
2. Okteto is a cloud provider that I have used in the past, and I wanted to have a deployed application.
Should a more generic example be used, this would require simply the change of the `rootUrl` variable in the `frontend/src/index.js` file, to have the proper API url. Also, because of automatic load balancers been provisioned by Okteto, I do not have to manage any of the ports, but the numbers requested are still there for reference. A proper implementation of the generic aproach described above would be to use evironment variables.

For some reason the `table2excel` library does not work. Ergo, this functionality will not be present
---
You can find the deployed app here: [Link](https://web-e-left.cloud.okteto.net/)