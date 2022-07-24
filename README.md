# InDMP

The software was developed by Filip Zoubek (https://orcid.org/0000-0003-1269-2668).

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [How to run](#how-to-run)
* [Limitations](#limitations)
* [Test cases](#test-cases)
* [License](#license)

## General info

Integrated The Machine-actionable Data Management planning application (InDMP) serves as a proof-of-concept for the thesis Framework for integration of reasearch data management services using machine-actionable data management plans. The application allows to integrate services using maDMPs to exchange information through the REST API, manage modification of each service as well as to track the evolution of DMPs and provenance of information. The repository contains the mentioned application for integration as well as the json file in which the test cases are stored ready for uploading to the Postman application. You can see the structure of the repository in the following listing:

```
indmp-app
│   src
│   LICENSE
│   pom.xml
│   README.md
│   test-cases.json
```

There is also API documentation of the InDMP application available on the [SwaggerHub](https://app.swaggerhub.com/apis/e11938258/InDMP/1.0.0#/).

## Technologies
The application was developed using Spring Boot in Java programming language version 11.0.13. The authorization between InDMP and Postman (client) is done using the OAuth2 protocol, where [Keycloak](https://www.keycloak.org/) 6.0.1 was used as the authorization server. The InDMP app also uses the PostgreSQL 10.19 tool as data storage. Tests were performed operating system on Ubuntu 18.04.6.

## Setup

The InDMP application has configuration file in /src/main/resources/application.properties. By default, all applications are configured that can run on the same machine with ports:

| Address| Service name |
| - | - |
| 127.0.0.1:8080 | InDMP app |
| 127.0.0.1:8090 | Keycloak |
| 127.0.0.1:5432 | PostgreSQL database |

### Keycloak

Every request has to be authorized before communicating with the application using the OAuth2 protocol. This is done using the [Keycloak](https://www.keycloak.org/) application, which needs to be set up properly after installation.

NOTE: The default port of the application is 8080, it is necessary to change it to port 8090 before starting the application, for example using the input argument:

```
-Djboss.socket.binding.port-offset=10
```

You need to perform the following actions:

1. Login to the Keycloak app, URL: http://127.0.0.1:8090/auth/admin/master/console/
2. Create a new realm with name "Services"
3. Select the realm "Services"
4. Create a new client scope with name "update"
5. Create a new client: "indmp_service" with settings:

| Property | Value |
| - | - |
| Client protocol | openid-connect |
| Valid Redirect URIs | * |
| Access Token Lifespan | 1 day |
| Assigned Default Client Scopes | update |

5. Create two new users: "dmp_app_1" and "repository_app_1" with password same as username.

That's it! 

When creating users, each of them will get a client id, which the InDMP application will use to recognize from which service the request was sent.

### PostgreSQL

The InDMP application uses the PostgreSQL 10 database system. After installation and logging into the system, you have to create a new user with database and grant neccessary provileges. Therefore, you need to perform the following actions:

1. Create a new user:

```SQL
CREATE USER indmp WITH PASSWORD 'indmp123';
```

2. Create a new database:

```SQL
CREATE DATABASE indmp WITH ENCODING 'UNICODE' LC_COLLATE 'C' LC_CTYPE 'C' TEMPLATE template0;
```

3. Grant all privileges on database to the user:

```SQL
GRANT ALL PRIVILEGES ON DATABASE indmp TO indmp;
```

That's it! By default, InDMP uses the following configuration:

| Property | Value |
| - | - |
| Host | 127.0.0.1 |
| Port | 5432 |
| Database | indmp |
| Username | indmp |
| Password | indmp123 |

NOTE: The InDMP application deletes the content of the tables each time it starts by default. If you want to change it, you need to modify the parameter initialization-mode in the application configuration from always to never.

## How to run

If PostgreSQL and Keycloak are running and are properly configured, you firstly build the InDMP application using the following command in terminal:

```console
> mvn clean package
```

and then run it:

```console
> java -jar target/indmp-app-1.0.0.jar
```

## Limitations

The current version is meant to be used as a proof-of-concept. A service can be registered and its rights restricted to particular maDMP classes. It can also use maDMP to create or update the DMP, modify the identification, and remove the class instance. It can also acquire the most recent version of maDMP and a list of identifiers' histories.

However, the solution has its disadvantages. The implementation cannot access prior maDMP versions and synchronizes data across all registered services, it does not limit services to a single maDMP. Simultaneously, when altering identifiers or removing instances, it ignores the class type and finds solely based on the location. Although unusual, two identical instances of distinct classes may share the same location within the specific maDMP. Furthermore, privileges can be restricted only to classes rather than specific properties, with the exception of the modified property, which must update all services. One of the solution's final flaws is that when altering an identifier or removing an instance, the service only checks the modification scope for the class that is being removed, not for the nested classes.

## Test cases

To verify the functionality of InDMP, a set of functional and non-functional test cases were created to model common situations in DMP development during the research. If you have Postman installed, import the test cases from the repository into your environment via the File menu. You will need to obtain a token from the authorization server before running the test cases. It can be get at the collection level, where the necessary information is preloaded. There are also general variables that can be changed at will as needed. In test case 6, one request has a different authorization due to the modification scope test. Therefore it is necessary to generate a new token, which can be obtained at the level of this request. 

Each new run of the application you must send two requests in the Init folder that register services - dmp tool and data repository. However, you need firstly to change the values of accessRights, to the correct client id from the application Keycloak, and endpointURL which should point to the endpoint where InDMP will send the new maDMP information after each modification. For testing purposes, this can be done using the [Webhook.site](https://webhook.site/) application, which will generate an API endpoint to receive requests. You just need one for both services.

### Modification scope of services

In order to understand all the tests, it is necessary to mention the modification scope of each testing service, you can see them in the following table:

| maDMP class | DMP app | Repository app |
| - | - | - |
| contact | Yes | Yes |
| contributor | Yes | Yes |
| cost | Yes | No |
| dataset | Yes | Yes |
| distribution | Yes | Yes |
| dmp | Yes | No |
| funding | Yes | No |
| grant id | Yes | No |
| host | Yes | Yes |
| license | Yes | Yes |
| metadata | Yes | Yes |
| project | Yes | No |
| security and privacy | Yes | Yes |
| technical resource | Yes | No |

NOTE: If the DMP is new, all values from the received data are stored.

### Functional and non-functional test cases

In the following two tables you can see the individual functional and non-functional test cases with steps and expected results. Each test case has its own folder in Postman, which consists of several consecutive requests. Requests also contain simple tests to verify the correctness of the response.

| Test case | Description | Steps | Expected code | Expected body |
| - | - | - | - | - |
| FTC1 | Testing the operation "maDMP update" | Send minimal maDMP with incomplete body | 400 |  |
|  |  | Send a new minimal maDMP with wrong timing | 404 |  |
|  |  | Send a new minimal maDMP | 200 |  |
|  |  | Send a new minimal maDMP again with same ID | 409 |  |
|  |  | Send the minimal maDMP with invalid modified property | 409 |  |
|  |  | Send long maDMP with same identifier | 200 |  |
| FTC2 | Testing the operation "get maDMP" | Send a new minimal maDMP | 200 |  |
|  |  | Get maDMP with incomplete parameters | 400 |  |
|  |  | Get maDMP with wrong parameters | 400 |  |
|  |  | Get maDMP with wrong identifiers | 404 |  |
|  |  | Get current version of maDMP | 200 | n. 1 |
| FTC3 | Testing the operation "update identifier" | Send a new maDMP with dataset | 200 |  |
|  |  | Update identifier with incomplete parameters | 400 |  |
|  |  | Update identifier with incomplete body | 400 |  |
|  |  | Update identifier with wrong specialization | 400 |  |
|  |  | Update identifier of dataset | 200 |  |
|  |  | Get current version of maDMP and verify the modification | 200 | n. 2 |
| FTC4 | Testing the operation "delete instance" | Send a new maDMP with dataset | 200 |  |
|  |  | Delete instance with incomplete parameters | 400 |  |
|  |  | Delete instance without body | 400 |  |
|  |  | Delete instance with wrong identifier | 404 |  |
|  |  | Delete instance with wrong class identifier | 404 |  |
|  |  | Delete instance | 200 |  |
|  |  | Get current version of maDMP and verify the deletions | 200 | n. 3 |
| FTC5 | Testing the operation "get identifier history" | Send a new maDMP with dataset | 200 |  |
|  |  | Update identifier of dataset | 200 |  |
|  |  | Get identifier history with wrong parameters | 400 |  |
|  |  | Get identifier history | 200 | n. 4 |
| FTC6 | Testing the modification scope | Send a new minimal maDMP | 200 |  |
|  |  | Send a new maDMP out of modification scope | 200 |  |
|  |  | Get current version of maDMP and verify the modification scope | 200 | n. 5 |
| FTC7 | Simulation of production env. 1 | Send a new minimal maDMP | 200 |  |
|  |  | Update maDMP with long body | 200 |  |
|  |  | Delete dataset | 200 |  |
|  |  | Update maDMP with the deleted dataset | 200 |  |
|  |  | Update maDMP with the old modified property | 409 |  |
| FTC8 | Simulation of production env. 2 | Send a new long maDMP with 3 datasets | 200 |  |
|  |  | Delete dataset 0 | 200 |  |
|  |  | Delete dataset 1 | 200 |  |
|  |  | Update identifier of dataset 2 to 0 | 200 |  |
|  |  | Delete dataset 0 | 200 |  |
|  |  | Delete project information | 200 |  |
|  |  | Get current version of maDMP | 200 | n. 6 |

| Test case | Description | Steps | Expected code | Expected time |
| - | - | - | - | - |
| NFTC1 | Testing creation time | Send a new minimal maDMP | 200 | ~428ms |
|  |  | Send a new long maDMP | 200 | ~564ms |
| NFTC2 | Testing update time | Send a new minimal maDMP | 200 | ~245ms |
|  |  | Update maDMP with short body | 200 | ~575ms |
|  |  | Update maDMP with long body | 200 | ~707ms |
|  |  | Update maDMP with short body | 200 | ~569ms |

#### Expected bodies

##### 1.

```json
{
    "dmp": {
        "created": "2022-04-14T10:00:50.000",
        "modified": "2022-04-14T10:00:50.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/00.0.9843"
        },
        "project": []
    }
}
```

##### 2.

```json
{
    "dmp": {
        "created": "2022-04-14T13:33:07.000",
        "modified": "2022-04-14T13:45:08.000",
        "contributor": [],
        "cost": [],
        "dataset": [
            {
                "description": "Some test scripts",
                "personal_data": "no",
                "sensitive_data": "no",
                "title": "Client application",
                "type": "Source code",
                "dataset_id": {
                    "identifier": "https://hdl.handle.net/0000/00.1234",
                    "type": "handle"
                },
                "distribution": [
                    {
                        "access_url": "https://hdl.handle.net/0000",
                        "available_until": "2030-09-30",
                        "byte_size": 1000000000,
                        "data_access": "open",
                        "title": "Planned distribution",
                        "host": {
                            "description": "GitHub is the best place to share code with friends, co-workers, classmates, and complete strangers. Over three million people use GitHub to build amazing things together. With the collaborative features of GitHub.com, our desktop and mobile apps, and GitHub Enterprise, it has never been easier for individuals and teams to write better code, faster. Originally founded by Tom Preston-Werner, Chris Wanstrath, and PJ Hyett to simplify sharing code, GitHub has grown into the largest code host in the world.",
                            "pid_system": [
                                "other"
                            ],
                            "storage_type": "repository",
                            "title": "GitHub",
                            "url": "https://www.re3data.org/repository/r3d100010375"
                        },
                        "license": [
                            {
                                "license_ref": "http://opensource.org/licenses/mit-license.php",
                                "start_date": "2020-09-30"
                            }
                        ]
                    }
                ],
                "metadata": [],
                "security_and_privacy": [],
                "technical_resource": []
            }
        ],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/11.2.22"
        },
        "project": []
    }
}
```

##### 3.

```json
{
    "dmp": {
        "created": "2022-04-14T14:48:39.000",
        "modified": "2022-04-14T14:50:20.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/33.3.12"
        },
        "project": []
    }
}
```

##### 4.

```json
[
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123",
        "specializationOf": "dmp:identifier",
        "value": "https://doi.org/10.0002/11.1.123",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.1234",
        "specializationOf": "dataset:identifier",
        "value": "https://hdl.handle.net/0000/00.00000",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "endedAtTime": "2022-04-14T14:55:41.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.1234",
        "specializationOf": "dataset:identifier",
        "value": "https://hdl.handle.net/0000/00.1234",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:41.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.1234/https://hdl.handle.net/0000",
        "specializationOf": "distribution:access_url",
        "value": "https://hdl.handle.net/0000",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    }
]
```

##### 5.

```json
{
    "dmp": {
        "created": "2022-04-25T07:04:05.000",
        "modified": "2022-04-25T07:27:43.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0002/17.7.189"
        },
        "project": []
    }
}
```

##### 6.

```json
{
    "dmp": {
        "created": "2022-04-25T07:38:01.000",
        "description": "This DMP is for our new project.",
        "ethical_issues_description": "Ethical issues are handled by ...",
        "ethical_issues_exist": "yes",
        "ethical_issues_report": "https://docs.google.com/document/d/xyz",
        "language": "eng",
        "modified": "2022-04-25T07:39:13.000",
        "title": "DMP for our new project",
        "contact": {
            "mbox": "john.smith@tuwien.ac.at",
            "name": "John Smith",
            "contact_id": {
                "identifier": "https://www.tiss.tuwien.ac.at/person/2351952424",
                "type": "other"
            }
        },
        "contributor": [
            {
                "mbox": "leo.messi@barcelona.com",
                "name": "Leo Messi",
                "role": [
                    "ProjectLeader"
                ],
                "contributor_id": {
                    "identifier": "https://orcid.org/0000-0002-0000-0000",
                    "type": "orcid"
                }
            },
            {
                "mbox": "robert@bayern.de",
                "name": "Robert Lewandowski",
                "role": [
                    "ContactPerson",
                    "DataManager"
                ],
                "contributor_id": {
                    "identifier": "https://orcid.org/0000-0002-4929-7875",
                    "type": "orcid"
                }
            },
            {
                "mbox": "CR@juve.it",
                "name": "Cristiano Ronaldo",
                "role": [
                    "DataCurator"
                ],
                "contributor_id": {
                    "identifier": "https://www.tiss.tuwien.ac.at/person/305962565",
                    "type": "other"
                }
            }
        ],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/17.1992/13.5.666"
        },
        "project": []
    }
}
```

## License

InDMP is licensed under the [MIT license](https://github.com/e11938258/InDMP/blob/main/LICENSE).
