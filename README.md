# TableTies
Automatically generate optimal seating arrangement

# Technologies
* kubernetes
* postgreSQL
* Java
* Spring boot 3
* angular

# How to run
* Run a kubernetes cluster.
* Run postgreSQL on the cluster using the following command:
```
kubectl apply -k k8s
```
* forward the port of the postgreSQL service to your local machine using the following command:
```
kubectl port-forward service/postgresql 5432:5432
```
* start the backend application. (add password to in environment variable "spring.datasource.password=YOUR_PASSWORD")
* start the frontend application.