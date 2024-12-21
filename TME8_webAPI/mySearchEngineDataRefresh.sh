cd [/path_to_tme10/]mySearchEngine &&
source ../mySearchEngineVEnv/bin/activate &&
python3 manage.py refreshOnSaleList >> ~/mySearchEngineLog &&
deactivate
