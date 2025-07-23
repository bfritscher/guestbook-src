#model_backend = 'pylist'
model_backend = 'dynamodb'

if model_backend == 'pylist':
    from .model_pylist import ModelPylist as model
elif model_backend == 'dynamodb':
    from .model_dynamodb import ModelDynamoDB as model
else:
    raise ValueError("No appropriate databackend configured. ")

appmodel = model()

def get_model():
    return appmodel
