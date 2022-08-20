# Micronaut AWS Lambda Rest Sample

## 開発環境（IntelliJ）の推奨設定

- アノテーション処理を有効化する。
    - Settings > Build, Excecution, Deployment > Compiler > Annotation Processor > `Enable Annotation Processing`をONにする。
- bootRunを実行している場合でもビルドされるようにする。（単一ファイルのビルドを実行すると spring-devtools が変更を検知して自動的に再起動されるようになります）
    - Intellij > Ctrl+Shift+A > type Registry... > `compiler.automake.allow.when.app.running`をONにする。
- Windowsの場合は、コンソール出力が文字化けするため、`C:¥Program Files¥JetBrains¥IntelliJ Idea xx.x.x¥bin`の中にある`idea64.exe.vmoptions`
  ファイルに`-Dfile.encoding=UTF-8`を追記する。

## Docker

```bash
$ ./gradlew composeUp
```

## ビルド & 起動

```bash
$ ./gradlew run
```

run タスクに -t を指定すると自動的に再起動するようになる。

```bash
$ ./gradlew run -t
```

## 動作確認

### curlでの動作確認

```bash
$ # ログイン
$ curl -s -L -X POST 'http://localhost:8080/api/auth/login' \
    -H 'Content-Type: application/json' \
    --data-raw '{
        "username": "test@example.com",
        "password": "passw0rd"
    }' | jq .
{
  "username": "c6218ca8-f6de-11ec-9536-0242ac110007",
  "roles": [
    "system_admin"
  ],
  "access_token": "<access-token>",
  "refresh_token": "<refresh-token>",
  "token_type": "Bearer",
  "expires_in": 3600
}

$ # トークンリフレッシュ
$ curl -s -L -X POST 'http://localhost:8080/api/auth/refresh' \
    -H 'Content-Type: application/json' \
    --data-raw '{
        "accessToken": "<access-token>",
        "refreshToken": "<refresh-token>"
    }' | jq .
{
  "username": "c6218ca8-f6de-11ec-9536-0242ac110007",
  "access_token": "<new-access-token>",
  "refresh_token": "<new-refresh-token>",
  "token_type": "Bearer",
  "expires_in": 3600
}

$ # 検索
$ curl -s -L -X GET 'http://localhost:8080/api/system/staffs' \
    -H 'Authorization: Bearer <access-token>' | jq .
{
  "page": 0,
  "perpage": 10,
  "count": 1,
  "totalPages": 1,
  "data": [
    {
      "createdAt": "2022-06-28T21:35:20",
      "updatedAt": "2022-06-28T21:35:20",
      "version": 1,
      "id": "c6218ca8-f6de-11ec-9536-0242ac110007",
      "firstName": "john",
      "lastName": "doe",
      "email": "test@example.com",
      "tel": "09011112222"
    }
  ],
  "success": true,
  "message": "正常終了"
}

$ # 登録
$ curl -s -L -X POST 'http://localhost:8080/api/system/staff' \
    -H 'Authorization: Bearer <access-token>' \
    -H 'Content-Type: application/json' \
    --data-raw '{
        "firstName": "jiro",
        "lastName": "yamada",
        "password": "aaaaaaaa",
        "passwordConfirm": "aaaaaaaa",
        "email": "aaaa@bbbb.com",
        "tel": "0000000000"
    }' | jq .
{
  "data": {
    "createdAt": "2022-06-29T15:21:31.424261",
    "updatedAt": "2022-06-29T15:21:31.424261",
    "version": 0,
    "id": "b4f15428-3211-4aed-bf75-5cdc16a33941",
    "firstName": "jiro",
    "lastName": "yamada",
    "email": "aaaa@bbbb.com",
    "tel": "0000000000"
  },
  "success": true,
  "message": "正常終了"
}

$ # 更新
$ curl -s -L -X PUT 'http://localhost:8080/api/system/staff/b4f15428-3211-4aed-bf75-5cdc16a33941' \
    -H 'Authorization: Bearer <access-token>' \
    -H 'Content-Type: application/json' \
    --data-raw '{
        "id": "b4f15428-3211-4aed-bf75-5cdc16a33941",
        "firstName": "jiro2",
        "lastName": "yamada",
        "password": "aaaaaaaa",
        "passwordConfirm": "aaaaaaaa",
        "email": "aaaa@bbbb.com",
        "tel": "0000000000",
        "version": 0
    }' | jq .
{
  "data": {
    "createdAt": "2022-06-29T15:21:31",
    "updatedAt": "2022-06-29T15:22:28.999502",
    "version": 1,
    "id": "b4f15428-3211-4aed-bf75-5cdc16a33941",
    "firstName": "jiro2",
    "lastName": "yamada",
    "email": "aaaa@bbbb.com",
    "tel": "0000000000"
  },
  "success": true,
  "message": "正常終了"
}

$ # 取得
$ curl -s -L -X GET 'http://localhost:8080/api/system/staff/b4f15428-3211-4aed-bf75-5cdc16a33941' \
    -H 'Authorization: Bearer <access-token>' | jq .
{
  "data": {
    "createdAt": "2022-06-29T15:21:31",
    "updatedAt": "2022-06-29T15:22:29",
    "version": 1,
    "id": "b4f15428-3211-4aed-bf75-5cdc16a33941",
    "firstName": "jiro2",
    "lastName": "yamada",
    "email": "aaaa@bbbb.com",
    "tel": "0000000000"
  },
  "success": true,
  "message": "正常終了"
}

$ # 削除
$ curl -s -L -X DELETE 'http://localhost:8080/api/system/staff/b4f15428-3211-4aed-bf75-5cdc16a33941' \
    -H 'Authorization: Bearer <access-token>' | jq .
{
  "data": {
    "createdAt": "2022-06-29T15:21:31",
    "updatedAt": "2022-06-29T15:22:29",
    "version": 1,
    "id": "b4f15428-3211-4aed-bf75-5cdc16a33941",
    "firstName": "jiro2",
    "lastName": "yamada",
    "email": "aaaa@bbbb.com",
    "tel": "0000000000"
  },
  "success": true,
  "message": "正常終了"
}
```

### Swagger UIでの動作確認

http://localhost:8080/api/swagger-ui/

### データベースの確認

```bash
mysql -h 192.168.64.3 -P 30306 -uroot -ppassw0rd micronaut-aws-lambda-rest-sample

mysql> show tables;
+--------------------------------------------+
| Tables_in_micronaut-aws-lambda-rest-sample |
+--------------------------------------------+
| code_categories                            |
| codes                                      |
| flyway_schema_history                      |
| holidays                                   |
| mail_templates                             |
| permissions                                |
| role_permissions                           |
| roles                                      |
| send_mail_queue                            |
| staff_roles                                |
| staffs                                     |
| upload_files                               |
| user_roles                                 |
| users                                      |
+--------------------------------------------+
14 rows in set (0.03 sec)
```

### ネイティブイメージを作成してAWS Lambdaにデプロイする

```bash
$ # build (spotless must be skipped, otherwise resource-config.json will be empty)
$ ./gradlew -x spotlessApply clean buildNativeLambda

$ # upload zip
$ aws lambda update-function-code --function-name micronaut-aws-lambda-rest-sample --zip-file fileb://build/libs/micronaut-aws-lambda-rest-sample-0.0.1-SNAPSHOT-lambda.zip
```

1. build/libs/micronaut-aws-lambda-rest-sample-0.0.1-SNAPSHOT-lambda.zip を AWS Lambda にアップロードする。
2. Lambda の Handler を「io.micronaut.function.aws.proxy.MicronautLambdaHandler」にする。

（参考）https://guides.micronaut.io/latest/mn-application-aws-lambda-graalvm-gradle-java.html

### API Gateway と連携する

1. API Gateway に「リソース」を追加する。「リソースの作成」をクリックして、「プロキシリソースとして設定する」にチェックをつける。
2. 作成された「ANY」メソッドをクリックして、「ANY - セットアップ」を表示する。
3. 統合タイプは「Lambda 関数プロキシ」を選択して、「Lambda 関数」は、前工程で作成した Lambda の関数名を入力する。
4. ステージを作成して、デプロイする。

（参考）https://docs.aws.amazon.com/ja_jp/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html

### AWS CLI (for test purpose)

```bash
$ # security group
$ aws ec2 create-security-group --group-name micronaut-aws-lambda-rest-db-sg --description "sg for rds"

$ # rds serverless cluster
$ aws rds create-db-cluster \
    --db-cluster-identifier micronaut-aws-lambda-rest-db-cluster \
    --engine aurora-mysql \
    --engine-version 8.0.mysql_aurora.3.02.0 \
    --serverless-v2-scaling-configuration MinCapacity=1,MaxCapacity=4 \
    --master-username root \
    --master-user-password passw0rd \
    --backup-retention 1 \
    --vpc-security-group-ids sg-092c41e691079e507

$ aws rds create-db-instance \
    --db-cluster-identifier micronaut-aws-lambda-rest-db-cluster \
    --db-instance-identifier micronaut-aws-lambda-rest-db-instance \
    --db-instance-class db.serverless \
    --engine aurora-mysql

$ # secret
$ aws secretsmanager create-secret \
    --name micronaut-aws-lambda-rest-secret \
    --secret-string '{"username":"root","password":"passw0rd","engine":"mysql","host":"micronaut-aws-lambda-rest-cluster.cluster-c4cosbx0klk0.ap-northeast-1.rds.amazonaws.com","port":"3306","dbClusterIdentifier":"micronaut-aws-lambda-rest-db-cluster"}'

$ # iam policy for secret
$ aws iam create-policy \
    --policy-name micronaut-aws-lambda-rest-db-proxy-policy \
    --policy-document '{
      "Version": "2012-10-17",
      "Statement": [
        {
          "Sid": "VisualEditor0",
          "Effect": "Allow",
          "Action": [
            "secretsmanager:GetResourcePolicy",
            "secretsmanager:GetSecretValue",
            "secretsmanager:DescribeSecret",
            "secretsmanager:ListSecretVersionIds"
          ],
          "Resource": [
            "arn:aws:secretsmanager:ap-northeast-1:YOUR_ACCOUNT_NUMBER:secret:micronaut-aws-lambda-rest-secret-JxU8GS"
          ]
        },
        {
          "Sid": "VisualEditor1",
          "Effect": "Allow",
          "Action": [
            "secretsmanager:GetRandomPassword",
            "secretsmanager:ListSecrets"
          ],
          "Resource": "*"
        }
      ]
    }'

$ # iam role for db proxy
$ aws iam create-role \
    --role-name micronaut-aws-lambda-rest-db-proxy-role \
    --assume-role-policy-document '{
     "Version": "2012-10-17",
     "Statement": [
      {
       "Sid": "",
       "Effect": "Allow",
       "Principal": {
        "Service": "rds.amazonaws.com"
       },
       "Action": "sts:AssumeRole"
      }
     ]
    }'

$ aws iam attach-role-policy --role-name micronaut-aws-lambda-rest-db-proxy-role --policy-arn arn:aws:iam::YOUR_ACCOUNT_NUMBER:policy/micronaut-aws-lambda-rest-db-proxy-policy

$ # security group
$ aws ec2 create-security-group --group-name micronaut-aws-lambda-rest-db-proxy-sg --description "sg for db proxy"

$ # rds proxy
$ aws rds create-db-proxy \
    --db-proxy-name micronaut-aws-lambda-rest-db-proxy \
    --engine-family MYSQL \
    --auth '[{"AuthScheme": "SECRETS", "SecretArn": "arn:aws:secretsmanager:ap-northeast-1:YOUR_ACCOUNT_NUMBER:secret:micronaut-aws-lambda-rest-secret-JxU8GS", "IAMAuth": "DISABLED"}]' \
    --role-arn arn:aws:iam::YOUR_ACCOUNT_NUMBER:role/micronaut-aws-lambda-rest-db-proxy-role \
    --vpc-subnet-ids subnet-d782febf subnet-a882fec0 subnet-c2fa8aea \
    --vpc-security-group-ids sg-0070970e27dae6ece

$ aws rds register-db-proxy-targets \
    --db-proxy-name micronaut-aws-lambda-rest-db-proxy \
    --db-cluster-identifiers micronaut-aws-lambda-rest-db-cluster

$ # security group
$ aws ec2 create-security-group --group-name micronaut-aws-lambda-rest-cache-sg --description "sg for redis"

$ # redis
$ aws elasticache create-cache-cluster \
    --cache-cluster-id micronaut-aws-lambda-rest-cache \
    --cache-node-type cache.t4g.micro \
    --engine redis \
    --engine-version 6.2 \
    --num-cache-nodes 1 \
    --cache-parameter-group default.redis6.x \
    --security-group-ids sg-0227b4aa89be3ddd2

$ # security group
$ aws ec2 create-security-group --group-name micronaut-aws-lambda-rest-sg --description "sg for lambda"

$ # iam role for lambda
$ aws iam create-role --role-name micronaut-aws-lambda-rest-role --assume-role-policy-document file://aws-cli/trust-policy.json
$ aws iam attach-role-policy --role-name micronaut-aws-lambda-rest-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
$ aws iam attach-role-policy --role-name micronaut-aws-lambda-rest-role --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole

$ # create function
$ aws lambda create-function --function-name micronaut-aws-lambda-rest-sample --runtime provided.al2 --zip-file fileb://build/libs/micronaut-aws-lambda-rest-sample-0.0.1-SNAPSHOT-lambda.zip --handler io.micronaut.function.aws.proxy.MicronautLambdaHandler --environment file://aws-cli/environments.json --role "arn:aws:iam::YOUR_ACCOUNT_NUMBER:role/micronaut-aws-lambda-rest-role"
```

## 参考情報

| プロジェクト                                                                                                 | 概要                              |
|:-------------------------------------------------------------------------------------------------------|:--------------------------------|
| [Lombok Project](https://projectlombok.org/)                                                           | 定型的なコードを書かなくてもよくする              |
| [Micronaut Guides](https://guides.micronaut.io/index.html)                                             | Micronaut Framework             |
| [Micronaut Security](https://micronaut-projects.github.io/micronaut-security/latest/guide)             | セキュリティ対策、認証・認可のフレームワーク          |
| [Micronaut Security JWT](https://micronaut-projects.github.io/micronaut-security/latest/guide)         | OAuth2、JWT認証・認可モジュール            |
| [Micronaut Custom AWS Lambda runtime](https://micronaut-projects.github.io/micronaut-aws/latest/guide) | GraalVMでカスタムランタイムをビルドするためのモジュール |
| [Project Reactor](https://projectreactor.io/)                                                          | リアクティブプログラミングのためのライブラリ          |
| [Micronaut Data R2DBC](https://micronaut-projects.github.io/micronaut-data/latest/guide)               | Reactive O/Rマッパー                |
| [Micronaut OpenAPI](https://micronaut-projects.github.io/micronaut-openapi/latest/guide)               | OpenAPI ドキュメントツール               |
| [Micronaut Flyway](https://micronaut-projects.github.io/micronaut-flyway/latest/guide)                 | DBマイグレーションツール                   |
| [Spock](http://spockframework.org/)                                                                    | テストフレームワーク                      |
