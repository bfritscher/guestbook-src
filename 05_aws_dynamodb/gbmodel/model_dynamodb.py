from .Model import Model
from datetime import datetime, timezone
import boto3

class ModelDynamoDB(Model):
    def __init__(self):
        self.resource = boto3.resource("dynamodb", region_name="us-east-1")
        self.table = self.resource.Table('guestbook')
        try:
            self.table.load()
        except:
            self.resource.create_table(
                TableName="guestbook",
                KeySchema=[
                    {
                        "AttributeName": "email",
                        "KeyType": "HASH"
                    },
                    {
                        "AttributeName": "date",
                        "KeyType": "RANGE"
                    }
                ],
                AttributeDefinitions=[
                    {
                        "AttributeName": "email",
                        "AttributeType": "S"
                    },
                    {
                        "AttributeName": "date",
                        "AttributeType": "S"
                    }
                ],
                ProvisionedThroughput={
                    "ReadCapacityUnits": 1,
                    "WriteCapacityUnits": 1
                }
            )

    def select(self):
        try:
            gbentries = self.table.scan()
        except Exception as e:
            return([['scan failed', '.', '.', '.']])

        # Parse the date string back to datetime object
        result = []
        for f in gbentries['Items']:
            try:
                # Parse ISO format
                date_obj = datetime.fromisoformat(f['date'].replace('Z', '+00:00'))
            except (ValueError, KeyError):
                date_obj = datetime.now()
            
            result.append([f['name'], f['email'], date_obj, f['message']])
        
        # in memory sort for demo app, but should be done via Global Secondary Index Partition
        result.sort(key=lambda x: x[2])
        return result

    def insert(self,name,email,message):
        gbitem = {
            'name' : name,
            'email' : email,
            'date' : datetime.now(timezone.utc).isoformat(),
            'message' : message
            }

        try:
            self.table.put_item(Item=gbitem)
        except:
            return False

        return True
