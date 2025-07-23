import gbmodel
import json

def lambda_handler(event, context):
    model = gbmodel.get_model()
    entries = [dict(name=row[0], email=row[1], signed_on=row[2].isoformat(), message=row[3] ) for row in model.select()]
    return {
        "statusCode": 200,
        "body": json.dumps(entries)
    }
