package com.bigtreetc.sample.micronaut.aws.lambda
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.function.aws.proxy.MicronautLambdaHandler
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class HomeControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    MicronautLambdaHandler handler = new MicronautLambdaHandler()

    @Shared
    Context lambdaContext = new MockLambdaContext()

    @Shared
    ObjectMapper objectMapper = handler.applicationContext.getBean(ObjectMapper)

    void "test handler"() {
        given:
        AwsProxyRequest request = new AwsProxyRequest()
        request.setHttpMethod("GET")
        request.setPath("/")
        when:
        AwsProxyResponse response = handler.handleRequest(request, lambdaContext)

        then:
        200 == response.getStatusCode().intValue()
        "{\"message\":\"Hello World\"}" == response.getBody()
    }
}
