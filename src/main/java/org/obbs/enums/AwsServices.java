package org.obbs.enums;

public enum AwsServices {
    EC2("ec2"),
    S3("s3"),
    HEALTHLAKE("healthlake"),
    LAMBDA("lambda"),
    DYNAMODB("dynamodb"),
    RDS("rds"),
    API_GATEWAY("execute-api"),
    IAM("iam"),
    SNS("sns"),
    SQS("sqs"),
    CLOUDFRONT("cloudfront"),
    CLOUDWATCH("monitoring"),
    ECS("ecs"),
    EKS("eks"),
    ELASTICACHE("elasticache"),
    ELASTIC_BEANSTALK("elasticbeanstalk"),
    KINESIS("kinesis"),
    GLUE("glue"),
    STEP_FUNCTIONS("states"),
    SECRETS_MANAGER("secretsmanager"),
    SYSTEMS_MANAGER("ssm"),
    CODEBUILD("codebuild"),
    CODEPIPELINE("codepipeline"),
    CLOUDTRAIL("cloudtrail"),
    ATHENA("athena"),
    REDSHIFT("redshift"),
    APP_RUNNER("apprunner"),
    DIRECT_CONNECT("directconnect"),
    ROUTE_53("route53"),
    TRANSCRIBE("transcribe"),
    TEXTRACT("textract"),
    REKOGNITION("rekognition"),
    GUARD_DUTY("guardduty"),
    MACIE("macie"),
    SECURITY_HUB("securityhub"),
    AUDIT_MANAGER("auditmanager"),
    COMPREHEND("comprehend"),
    PERSONALIZE("personalize"),
    FRAUD_DETECTOR("frauddetector"),
    CONNECT("connect"),
    PINPOINT("mobiletargeting"),
    TIER_1_SUPPORT("support");

    private final String serviceCode;

    AwsServices(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }
}
