const int valuesForAverage = 10;
float readValues[valuesForAverage];

void setValueForAverage(int valuePosition, float value){
  readValues[valuePosition] = value;
}

int getNumValuesForAverage(){
  return valuesForAverage;
}

int cmp (const void* num1,const void* num2)
{
  float a = *((float *)num1);
  float b = *((float *)num2);
  if (a < b) return -1;
  else if (a == b) return 0;
  return 1;
}


float mediumValue() {
  float average = 0;
  int numItems = 0;
  qsort (readValues ,valuesForAverage,sizeof(float),cmp);
  for (int i = 2; i < (valuesForAverage)-2;i++) {
    average = average + readValues[i];
    numItems ++ ;
  }
  return average/numItems;
}


