# Welcome to "TELR" ![](https://img.shields.io/github/v/tag/lordkada/telr)

The goal of "TELR" is to implement a simple API service that translates a Pokeman to a Shakespearean description.

# The development process

I develop this service during my spare time, so you can monitor the progress of my efforts [here](https://github.com/lordkada/telr/milestones?direction=asc&sort=due_date&state=open).

Basically, I will develop using a [Gitflow](https://leanpub.com/git-flow/read) approach and release new versions of the software according to the milestones above.

# Execute tests

To execute tests, type from inside the project folder
```
$ ./mvnw test
```

# Docs

In the [docs](/docs) folder you can find the Postman collection for this project

# Run the software

To run the API from the terminal, you have a couple of alternatives:

- using your local JVM (JDK1.8+)
- run a docker container

## Run locally (JVM)
type from inside the project folder:

```
$ ./mvnw spring-boot:run
```

## Run inside a Docker container
follow these simple steps:

1. Build the docker image locally:
```
$ devops/build_docker_image.sh
```

2. Check-out the result and notice the name/tag of the created image in the very few last lines of log. Eg:
   
```
[INFO] Successfully built image 'docker.io/library/telr:1.0.0-SNAPSHOT'
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  11.107 s
[INFO] Finished at: 2020-12-13T18:35:40+01:00
[INFO] ------------------------------------------------------------------------
```

In the example above the name/tag is: **telr:1.0.0-SNAPSHOT**

3. Run the docker container with the following command (please, replace the name/tag according to the step above):
```
$ docker run -it -d -p8080:8080 telr:1.0.0-SNAPSHOT
```

# Show me how to use TELR

Once started, here you have the endpoint available.


## Get a Pokemon Shakespearean description
### HTTP/GET ==> /pokemon/{pokemon-name}

#### Parameter:
_pokemon-name_: The name of the Pokemon you want to be described 

####Result:

**SUCCESS**: 
- HTTP status 200 
- Json object containing the _name_ and _description_ of the Pokemon. 
  

Eg:
```
$ curl --location --request GET 'localhost:8080/pokemon/charizard'
```

produces:

```
{
 "name": "charizard",
 "description": "Charizard flies 'round the sky in search of powerful opponents. 't breathes fire of such most wondrous heat yond 't melts aught. However,  't nev'r turns its fiery breath on any opponent weaker than itself."
}
```

**ERROR**:
- HTTP status 404: Pokemon name not found.
- HTTP status 429: Too many requests (please, notice that you're limited to 5 requests per hour and 60 per day)