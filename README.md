# RIA2 - BACKEND

### Prerequisites

Here are the prerequisites for finding work on the project.

- IntelliJ IDEA 2023.2.2 (Ultimate Edition)
- Apache Maven 3.9.5
- Google Cloud Platform account with Vision API V1 enabled
- Eclipe Temurin version 21.0.1
- jUnit 5.8.1

## DataObject

This Java microservice offers essential functionalities for Google Cloud Storage, such as file upload/download, existence checks, URL publishing, and deletion operations. It's designed for efficient cloud storage management with easy URI-based interactions.

### Deployment

#### On dev environment

**Dependencies**

Go the the project root and run the following command to install the dependencies.

```
mvn clean install
```

#### On prod environment

**Build**

```
mvn clean package
```

### Directory Structure 

```
.
├── main
│   ├── java
│   │   └── com
│   │       └── ymd
│   │           └── dataobject
│   │               ├── DataobjectApplication.java
│   │               ├── controller
│   │               │   └── DataObjectController.java																//Here are defined the endpoints
│   │               ├── exception																										//Exceptions
│   │               │   ├── DataObjectImplException.java
│   │               │   ├── NotEmptyObjectException.java
│   │               │   ├── ObjectAlreadyExistsException.java
│   │               │   └── ObjectNotFoundException.java
│   │               ├── model																												
│   │               │   ├── PublishRequest.java																			//Publish endpoint request
│   │               │   └── UploadRequest.java																			//Upload endpoint request
│   │               └── service
│   │                   └── storage
│   │                       ├── GoogleDataObjectImpl.java														//Implementation of the interface with GCP
│   │                       └── IDataObject.java																		//DataObject Interface
│   └── resources
│       ├── application.yml																													//Spring basic configration of the micro-ser.
│       ├── static
│       └── templates
└── test
    └── java
        └── com
            └── ymd
                └── dataobject
                    └── DataobjectApplicationTests.java															//Unit tests
```

## LabelDetector

### Description

This Java microservice uses Google Cloud Vision API to extract labels from images, offering customizable label limits and confidence thresholds. It outputs concise, JSON-formatted label data.

### Configuration

**Environment File**

The project contains an example of the environment file required for the project. Simply copy it and rename it .env.

```
# .env
GOOGLE_APPLICATION_CREDENTIALS="path/to/credentials.json"
```

### Deployment

#### On dev environment

**Dependencies**

Go the the project root and run the following command to install the dependencies.

```
mvn clean install
```

#### On prod environment

**Build**

```
mvn clean package
```

## Directory structure

```
.
├── main
│   ├── java
│   │   └── com
│   │       └── ymd
│   │           └── labeldetector
│   │               ├── LabeldetectorApplication.java													
│   │               ├── controller
│   │               │   └── LabelController.java																//Here are defined the endpoints
│   │               ├── model
│   │               │   ├── AnalyzeRequest.java																	//Analysis request Model  
│   │               │   └── Label.java																					//Label Model
│   │               └── service
│   │                   └── vision
│   │                       ├── GoogleLabelDetectorImpl.java										//Implementation of interface with Google Vision
│   │                       └── ILabelDetector.java															//LabelDetector Interface 
│   └── resources
│       ├── application.yml																											//Spring basic configuration for the micro-serv.
│       ├── static
│       └── templates
└── test
    └── java
        └── com
            └── ymd
                └── labeldetector
                    └── LabeldetectorApplicationTests.java											//Unit tests
```

## Collaborate

#### How to propose a new feature ?

If you're interested in enhancing this project, you're welcome to:

- **Fork the Project:** You can create a fork of the project on your own GitHub account to work on your changes.
- **Submit Pull Requests:** If you develop new features or improvements, feel free to submit a pull request for integration into the main project.

#### Commit Rules

I use very simple commit rules. The commit message **starts with an infinitive verb and describes the added/deleted content clearly in one sentence**. If your commit requires two sentences because the code added modifies two things, please make two separate commits. This is to improve readability and also simplify versioning.

#### Branches Strategy

By default, there are two branches: main and develop. Main is the branch currently in production, develop is based on the same branch.

To add a new feature, please create a branch from develop using Gitflow. [To the Gitflow guide](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)

#### Any question ?

For any questions or further information, please feel free to reach out to me at: [yann.menoud@gmail.com](mailto:yann.menoud@gmail.com).

## License

[This project is under MIT Licence](https://github.com/menoudyann/BI_LabelDetector/blob/main/LICENSE).

## Contact

You can contact me by email at the following address: [yann.menoud@gmail.com](mailto:yann.menoud@gmail.com) or directly on [Linkedin](https://www.linkedin.com/in/yann-menoud-433780225/).