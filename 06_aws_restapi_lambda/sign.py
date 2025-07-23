import gbmodel
import json

def lambda_handler(event, context):
    model = gbmodel.get_model()
    request_json = json.loads(event.get("body", "{}"))

    if all(key in request_json for key in ("name", "email", "message")):
        model.insert(
            request_json["name"], request_json["email"], request_json["message"]
        )
        print(f"Received: {request_json['name']}, {request_json['email']}, {request_json['message']} and added to DB")
        return {
            "statusCode": 204,
            "body": "",
        }
    else:
        return {
            "statusCode": 400,
            "body": json.dumps(
                {"error": "JSON missing name, email, or message property"}
            ),
        }
