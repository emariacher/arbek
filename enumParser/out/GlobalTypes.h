typedef struct UINT24_TAGUP  // unpacked structure
{
    UInt8                   low;                        ///< (o  0, s  1, a 1) 
    UInt8                   mid;                        ///< (o  1, s  1, a 1) 
    UInt8                   high;                       ///< (o  2, s  1, a 1) 
} UINT24_TUP;  // size: 3, alignment: 1

typedef enum
{
    SET_T_OFF                                = 0,  ///< 
    SET_T_ON                                 = 1,  ///< 
} SET_T;

typedef enum
{
    MODE_T_WRITE_ONLY                        = 0,  ///< 
    MODE_T_READ_ONLY                         = 1,  ///< 
    MODE_T_READ_WRITE                        = 2,  ///< 
} MODE_T;

typedef enum
{
    REG_T_PSAT                               = 0,  ///< 
} REG_T;

typedef enum
{
    TRANSACTION_STATE_T_CLOSED               = 0,  ///< 
    TRANSACTION_STATE_T_OPEN                 = 1,  ///< 
    TRANSACTION_STATE_T_UPDATE_OPEN          = 2,  ///< 
    TRANSACTION_STATE_T_UPDATE_READY         = 3,  ///< 
    TRANSACTION_STATE_T_UPDATE_REPLACED      = 4,  ///< 
    TRANSACTION_STATE_T_UPDATE_RETRY         = 5
} TRANSACTION_STATE_T;

typedef enum
{
    VNVM_T_RATIO3_FULLCLK_BYPASSDISABLED     = 12,  ///< 
    VNVM_T_RATIO3_FULLCLK_BYPASSENABLED      ,  ///< 
    VNVM_T_RATIO3_CLKDIV4_BYPASSDISABLED     ,  ///< 
    VNVM_T_RATIO3_CLKDIV4_BYPASSENABLED      ,  ///< 
    VNVM_T_RATIO2_FULLCLK_BYPASSDISABLED     ,  ///< 
    VNVM_T_RATIO2_FULLCLK_BYPASSENABLED     ,  ///< 
    VNVM_T_RATIO2_CLKDIV4_BYPASSDISABLED     ,  ///< 
    VNVM_T_RATIO2_CLKDIV4_BYPASSENABLED      ,  ///< 
} VNVM_T;

typedef enum
{
    MUTE_T_ON                                ,  ///< 
    MUTE_T_OFF                               ,  ///< 
    MUTE_T_KEEP                              ,  ///< 
} MUTE_T;

typedef enum
{
    ERROR_CODE_T_NONE                        = 0,  ///< 
    ERROR_CODE_T_QUEUE                       = 1,  ///< 
    ERROR_CODE_T_MEMORY                      = 2,  ///< 
    ERROR_CODE_T_TIMEOUT                     = 3,  ///< 
    ERROR_CODE_T_SEQUENCE                    = 14,  ///< 
    ERROR_CODE_T_CRC                         ,  ///< 
    ERROR_CODE_T_ID                          = 6,  ///< 
    ERROR_CODE_T_RX_IF                       = 7,  ///< 
    ERROR_CODE_T_VERSION                     = 8,  ///< 
    ERROR_CODE_T_LENGTH                      ,  ///< 
    ERROR_CODE_T_DESTINATION                 ,  ///< 
    ERROR_CODE_T_UNKNOWN_ID                  ,  ///< 
    ERROR_CODE_T_UNKNOWN                     ,  ///< 
} ERROR_CODE_T;

typedef enum
{
    STATUS_T_OK                              ,  ///< 
    STATUS_T_ERROR                           ,  ///< 
    STATUS_T_NOT_AVAILABLE                   ,  ///< 
    STATUS_T_TIMEOUT                         ,  ///< 
    STATUS_T_BUSY                            ,  ///< 
} STATUS_T;

/* cowabunga */
