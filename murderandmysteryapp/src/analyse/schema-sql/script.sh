#!/bin/bash
# 
# auteur: Dominique Huguenin (dominique.huguenin AT rpn.ch)
HOST=db.lxd
PORT=5432
USER=murderandmystery
PASSWORD=murderandmysterypass
DATABASE=murderandmysteryDB
PSQL_SCRIPT=script.psql

PGPASSWORD=$PASSWORD psql -h $HOST -p $PORT -U $USER $DATABASE -f $PSQL_SCRIPT
