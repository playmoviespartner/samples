# Play Movies Partner API sample
Sample code to use the Play Movies Partner API

## Building

Install [Maven](https://maven.apache.org) and Java SDK 1.6 and run:

```
mvn package
```

## Running

The demo app uses Google's [Application Default Credentials](https://developers.google.com/identity/protocols/application-default-credentials) mechanism to generate an OAuth2 token, so you need to download generate a JSON key, download it, then set the `GOOGLE_APPLICATION_CREDENTIALS` pointing to the file location. Example:

```
export GOOGLE_APPLICATION_CREDENTIALS="$HOME/my_project.json"
```

Once that variable is set, you can call

```
mvn test -Dexec.args="account_id batch_size number_calls"
```

where:

`account_id` is your Partner Account ID (from the [Partner Portal](https://partnerdash.google.com/partnerdash/d/tvfilm))  
`batch_size` defines the number of orders returned per API call  
`number_calls` defines the number of API calls made  

Example:

```
mvn test -Dexec.args="4815162342 50 2"
```
