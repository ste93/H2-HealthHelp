const int valuesForAverage = 10;
float readValues[valuesForAverage];

/**
 * insert the value into the position valuePosition of the array
 * @param valuePosition the position of the array
 * @param value the value to insert
 */
void setValueForAverage(int valuePosition, float value){
  readValues[valuePosition] = value;
}

/**
 * @returns the number of the elements of the array
 */
int getNumValuesForAverage(){
  return valuesForAverage;
}

/**
 * a function that compares two float numbers needed for the quickSort
 * @param num1 the first number 
 * @param num2 the second number
 * @returns
 *    1 if a > b
 *    0 if a = b
 *    -1 if a < b
 */
int compare (const void* num1,const void* num2)
{
  float a = *((float *)num1);
  float b = *((float *)num2);
  if (a < b) return -1;
  else if (a == b) return 0;
  return 1;
}


/**
 * calculates the medium value of the array avoiding the 2 lowest numbers and the 2 highest
 * @returns the average of the array
 */
float mediumValue() {
  float average = 0;
  int numItems = 0;
  qsort (readValues ,valuesForAverage,sizeof(float),compare);
  for (int i = 2; i < (valuesForAverage)-2;i++) {
    average = average + readValues[i];
    numItems ++ ;
  }
  return average/numItems;
}


