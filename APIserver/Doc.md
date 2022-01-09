

### Design considerations

The browser as a client can access 127.0.0.1:9527.

At first it can process all "and" or all "or" query, I want to improve it so that it can process "and"  "or" mixed query.  "and" has higher precedence.

#### Example using

`127.0.0.1:9527/?query=<C1 == "A" or C2 &= "B" >`

`127.0.0.1:9527/?query=<C1 == "Test" and * $= "Prod" and * != "Hidden" >`



#### test case

| C1   | C2   | C3     | test   |
| ---- | ---- | ------ | ------ |
| A    | B    |        | for == |
| Test | Prod | Hidden | for != |
| Test |      |        | for != |
| A    | BA   |        | for &= |
|      |      |        | for $= |



#### Acceleration

1. To reduce comparison times, we could query the first condition in all rows.
2. if it is "or" conjunction, we can iterate each query since we can write into the file when one condition is satisfied. If it is an "and" conjunction, we can iterate each line since we need to consider all conditions.
3. iterating each query may cause duplication, thus, we need to maintain a sorted set. Sorted by line number.



#### ErrorCode

| 1    | have more than one operators              |
| ---- | ----------------------------------------- |
| 2    | both "or" "and" appear in query           |
| 3    | query form error                          |
| 4    | column name error, send .csv file as hint |
| 5    | for future use                            |
| 6    | for future use                            |
| 7    | for future use                            |
| 8    | for future use                            |
| 0    | success                                   |
| -1   | don't have data                           |









### Lessons-learnt

1. In  `if (querystr == "queryFormError")` Content-Length = 100, browser can not receive any http data. 

   Reason: Content-Length > Actual Length

    If Content-Length is greater than actual length, the server/client will wait for the next byte after reading the end of the message, and naturally there will be no response until the timeout.

   Actually, I met some troubles in send http packet. I learned that HTTP has strict content length constrain. I need use another better way to send HTTP packet. I learned why we need some framework like spring mvc ，spring boot.  

2. when content including `<C1`,  browser can not receive any http data.

   I tried `&lt;`.  and use `content.length()` to specify length instead of  100. It worked.

3. I learned that we could use File class `createTempFile` `temp.deleteOnExit()`method and then we don't manually delete so many files.

4. We could self define `Treeset` comparable to sort and distinguish.

4. It is difficult to deal with `* match`,  `* != "Hidden"` is ambiguous, does it return true if any column is not equal to "Hidden", or does it return true when all columns are not equal to "Hidden"?
   I define that all columns are not equal to hidden returns true.
   
   

Success screenshot

![image-20220109114000776](C:\Users\12638\AppData\Roaming\Typora\typora-user-images\image-20220109114000776.png)

![image-20220109113900693](C:\Users\12638\AppData\Roaming\Typora\typora-user-images\image-20220109113900693.png)
