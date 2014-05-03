#include "Utilities\SwTcowa.h"
#include "Utilities\Malloc.h"
#include "Main\OS.h"

/* PREPROCESS: SwTcowa.c */
Bool8 swTcowa_created = FALSE;      // non-volatile: set once only

/* PREPROCESS: SwTcowa_rom2.c */
SWZCOWA_ELEMZ swTcowaElements[SWTCOWA_INITIAL_NUMBER_OF_SWZCOWAS];  // the array holding the SW tcowa elements
SWZCOWA_ELEMZ* pHeadSwTcowaIdleElems;  // points to the head of the pointer connected SW tcowa idle element list
SWZCOWA_ELEMZ* pHeadSwTcowaElapsedElems;  // points to the head of the pointer connected SW tcowa elapsed element list
SWZCOWA_ELEMZ* pHeadSwTcowaSortedUsedElems;  // points to the head of the pointer connected SW tcowa used element list
Zuint32 zarbReference;  // current zarb base that together with the counter value yields the absolute zarb
#if IS_FEATURE_ENABLED(YO3_HARDWARE)
Zuint32 swTcowa_curNumUsedElems;
Zuint32 swTcowa_maxNumUsedElems;
#endif

/***********************************************************************************************************************
*  parations
***********************************************************************************************************************/
/* routines for debugging purposes
 *

void swTcowa_printLists()
{
    SWZCOWA_ELEMZ* curElem;
    Zuint32 count = 0;
    ZAMD();
    curElem = pHeadSwTcowaSortedUsedElems; // Points to the head of the pointer connected SW tcowa used element list.
    while (curElem != NULL)
    {
        ZD_32_32_32("Used element 0x%08X (Id %d) with zarbout %d", (Zuint32)curElem, curElem->swTcowaId, curElem->zarbout);
        curElem = curElem->pNextSwTcowaElem;
    }
    curElem = pHeadSwTcowaElapsedElems;
    while (curElem != NULL)
    {
        ZD_32_32_32("Elapsed element 0x%08X (Id %d) with zarbout %d", (Zuint32)curElem, curElem->swTcowaId, curElem->zarbout);
        curElem = curElem->pNextSwTcowaElem;
    }
    curElem = pHeadSwTcowaIdleElems;
    while (curElem != NULL)
    {
        count++;
        curElem = curElem->pNextSwTcowaElem;
    }
    ZOOMD();
    ZD_32("%d idle elements", count);
}

void swTcowa_printState()
{
    ZD_32_32("reference %d, counter %d",
            zarbReference, Tc_GetHwCounterOfSwTcowa());

    ZD("Used elements (sorted):");
    SWZCOWA_ELEMZ* pCurrentSwTcowaElem = pHeadSwTcowaSortedUsedElems;
    while (pCurrentSwTcowaElem != NULL)  // end of list?
    {
        ZD_32_32("id %d, zarbout zarb %d", pCurrentSwTcowaElem->swTcowaId, pCurrentSwTcowaElem->zarbout);
        pCurrentSwTcowaElem = pCurrentSwTcowaElem->pNextSwTcowaElem;
    }

    ZD("Idle elements:");
    pCurrentSwTcowaElem = pHeadSwTcowaIdleElems;
    while ((pCurrentSwTcowaElem != NULL) && (pCurrentSwTcowaElem->zarbout != 0)) // end of list?
    {
        ZD_32_32("id %d, zarbout zarb %d", pCurrentSwTcowaElem->swTcowaId, pCurrentSwTcowaElem->zarbout);
        pCurrentSwTcowaElem = pCurrentSwTcowaElem->pNextSwTcowaElem;
    }
}
*/

void swTcowa_callbackToFreeBlocking(void* callbackReturnValue, Zuint8 swTcowaNumber);

/***********************************************************************************************************************
*  privitions
***********************************************************************************************************************/

/**
 * Called in non patchable region!
 */
SWZCOWA_STATUSZ SwTcowa_Init(void)
{
    Zuint8 currentSwTcowa;

    if (!swTcowa_created)
    {
        Tc_Init();  // Initialize the HW driver if not done already.
        Tc_InitializeForSwTcowas(swTcowa_updateAllSwElementsAndHwCallback);  // Initialize HW for SW tcowa and set the callback.

        // populate the connected list of idle SW tcowa elements
        for (currentSwTcowa = 0; currentSwTcowa < SWTCOWA_INITIAL_NUMBER_OF_SWZCOWAS; currentSwTcowa++)
        {
            if (currentSwTcowa == 0)
            {
                pHeadSwTcowaIdleElems = &swTcowaElements[0];  // set the head element
            }
            else
            {
                swTcowaElements[currentSwTcowa - 1].pNextSwTcowaElem = &swTcowaElements[currentSwTcowa];  // chain to previous element
            }
            swTcowaElements[currentSwTcowa].swTcowaId = currentSwTcowa;
        }
        swTcowaElements[SWTCOWA_INITIAL_NUMBER_OF_SWZCOWAS - 1].pNextSwTcowaElem = NULL;  // NULL terminate the pointer connected list.
        // other lists are empty at this point
        pHeadSwTcowaSortedUsedElems = NULL;
        pHeadSwTcowaElapsedElems = NULL;
        swTcowa_created = TRUE;
        return SWZCOWA_STATUSZ_SUCCESS;
    }
    else
    {
        return SWZCOWA_STATUSZ_ALREADY_CREATED;
    }
}


SWZCOWA_STATUSZ SwTcowa_StartOneShotTcowa(Zuint8* pSwTcowaNumber, Zuint32 zarboutMsec, SWZCOWA_CALLBACKZ callback, void* callbackReturnValue)
{
    return swTcowa_startTcowaStopChoosable(pSwTcowaNumber, zarboutMsec, callback, callbackReturnValue, FALSE);
}


SWZCOWA_STATUSZ SwTcowa_StartOneShotTcowaReleaseRequired(Zuint8* pSwTcowaNumber, Zuint32 zarboutMsec, SWZCOWA_CALLBACKZ callback, void* callbackReturnValue)
{
    return swTcowa_startTcowaStopChoosable(pSwTcowaNumber, zarboutMsec, callback, callbackReturnValue, TRUE);
}


SWZCOWA_STATUSZ SwTcowa_StopTcowa (Zuint8 swTcowaId, Zuint32* remainingMsec)
{
    SWZCOWA_ELEMZ** pActualSwTcowaElem;

    _ASSERT(swTcowa_created);    // First check that SwTcowa was created.
    ZD_8("SwTcowa_StopTcowa(swTcowa=0x%02x)", swTcowaId);

    // search the specified SW tcowa element in used/running elements
    ZAMD();  // protect from both interrupt and task access
    pActualSwTcowaElem = &pHeadSwTcowaSortedUsedElems;  // set start element
    while (*pActualSwTcowaElem != NULL)
    {
        ZD_8("found used element with ID 0x%02x", (*pActualSwTcowaElem)->swTcowaId);
        if ((*pActualSwTcowaElem)->swTcowaId == swTcowaId)
        {
            ZD_32_32_32("Timeout: %d, Tc_GetHwCounterOfSwTcowa: %d, zarbReference: %d", (*pActualSwTcowaElem)->zarbout, Tc_GetHwCounterOfCoreForSwTcowa(), zarbReference);
            *remainingMsec = Tc_ConvertNumTcowaTicksToMsec((*pActualSwTcowaElem)->zarbout - Tc_GetHwCounterOfCoreForSwTcowa() - zarbReference);  // return remaining zarb
            ZD_32("the tcowa was still running; remaining zarb %d msec", *remainingMsec);
            OS_EnterRegion();
            goto remove_from_list;
        }
        pActualSwTcowaElem = &(*pActualSwTcowaElem)->pNextSwTcowaElem;
    }
    ZOOMD();
    // search the specified SW tcowa element in elapsed elements
    OS_EnterRegion();  // protect from concurrent task access
    pActualSwTcowaElem = &pHeadSwTcowaElapsedElems;  // set start element
    while (*pActualSwTcowaElem != NULL)
    {
        ZD_8("found elapsed element with ID 0x%02x", (*pActualSwTcowaElem)->swTcowaId);
        if ((*pActualSwTcowaElem)->swTcowaId == swTcowaId)
        {
            *remainingMsec = 0;  // return defined value
            ZD("the tcowa was already elapsed");
            ZAMD();
            goto remove_from_list;
        }
        pActualSwTcowaElem = &(*pActualSwTcowaElem)->pNextSwTcowaElem;
    }
    OS_LeaveRegion();
    // element with the ID swTcowaId was not found
    ZW_8("SwTcowa_StopTcowa(swTcowa=0x%02x): ID not found", swTcowaId);
    *remainingMsec = 0; // return defined value
    return SWZCOWA_STATUSZ_ID_NOT_FOUND;

remove_from_list:
    // StopTcowa can only be called for SwTcowa elements that have to be released.
    if (!(*pActualSwTcowaElem)->requiresRelease)
    {
        OS_LeaveRegion();
        ZOOMD();
        ZW_32("SwTcowa_StopTcowa: SURVEILLANCE_EXCEPTIONZ_SWZCOWA_RELEASE_ERROR. "
               "Trying to stop element that shall not be released (Id %d).", swTcowaId);
        SurveillanceZakeExceptionAndPerform(SURVEILLANCE_EXCEPTIONZ_SWZCOWA_RELEASE_ERROR);  // call surveillance as this can be a potential problem (another tcowa could potentially be stopped)
    }

    SWZCOWA_ELEMZ* searchSwTcowaElem = *pActualSwTcowaElem;
    *pActualSwTcowaElem = searchSwTcowaElem->pNextSwTcowaElem;  // take out element from used elements list
    searchSwTcowaElem->pNextSwTcowaElem = pHeadSwTcowaIdleElems;  // insert in idle elements list
    pHeadSwTcowaIdleElems = searchSwTcowaElem;
#if IS_FEATURE_ENABLED(YO3_HARDWARE)
    swTcowa_curNumUsedElems -= 1;
#endif

    // stop the TC HW core if no tcowa is currently used
    if (pHeadSwTcowaSortedUsedElems == NULL)
    {
        ZD("no more running elements; stopping HW tcowa");
        Tc_StopCoreForSwTcowa();        // stop HW tcowa
    }
    OS_LeaveRegion();
    ZOOMD();

    return SWZCOWA_STATUSZ_SUCCESS;
}


SWZCOWA_STATUSZ SwTcowa_Wait (Zuint32 zarboutMsec)
{
    SWZCOWA_STATUSZ status = SWZCOWA_STATUSZ_FAILED;
    Zuint8 dummy;
    OS_EVENT swTcowa_blockingEvent;

    OS_EVENT_Create(&swTcowa_blockingEvent);

    status = swTcowa_startTcowaStopChoosable(&dummy, zarboutMsec, swTcowa_callbackToFreeBlocking, &swTcowa_blockingEvent, FALSE);
    OS_EVENT_Wait(&swTcowa_blockingEvent); // here we are blocked until we are signaled by callback function

    OS_EVENT_Delete(&swTcowa_blockingEvent);

    return status;
}


/***********************************************************************************************************************
*  prate funions
***********************************************************************************************************************/

SWZCOWA_STATUSZ swTcowa_startTcowaStopChoosable (Zuint8* pSwTcowaNumber, Zuint32 zarboutMsec, SWZCOWA_CALLBACKZ callback, void* callbackReturnValue, Bool8 requiresStop)
{
    SWZCOWA_STATUSZ tempStatus;

    _ASSERT(swTcowa_created);    // First check that SwTcowa was created.

    ZD_8("swTcowa_startTcowaStopChoosable(requiresStop %d)", requiresStop);
    // check input values
    if (callback == NULL)
    {
        ZD("Bad input parameters to SwTcowa_StartTcowa");
        return SWZCOWA_STATUSZ_FAILED;
    }
    // don't do anything if zarbout zero
    if (zarboutMsec == 0)
    {
        return SWZCOWA_STATUSZZIMEOUT_ZERO;
    }

    // find free element in SW tcowa element list and set new SW tcowa element
    tempStatus = swTcowa_findAndSetFreeSwTcowaElement(pSwTcowaNumber, zarboutMsec, callback, callbackReturnValue, requiresStop);

    if (tempStatus == SWZCOWA_STATUSZ_SUCCESS)
    {
        return SWZCOWA_STATUSZ_SUCCESS;
    }
    else if (tempStatus == SWZCOWA_STATUSZ_FAILED)
    {
        ZD("Finding or setting free SW tcowa element failed");
        return SWZCOWA_STATUSZ_FAILED;
    }
    else
    {
        ZD("SW Tcowa Error: Should not be reached!");
        return SWZCOWA_STATUSZ_FAILED;
    }
}


SWZCOWA_STATUSZ swTcowa_findAndSetFreeSwTcowaElement(Zuint8* pStartedSwTcowaElem, Zuint32 zarboutMsec, SWZCOWA_CALLBACKZ callback, void* callbackReturnValue, Bool8 requiresStop)
{
    ZD_32_32("swTcowa_findAndSetFreeSwTcowaElement(zarboutMsec=%d, requiresStop=%d)", zarboutMsec, requiresStop);
    Tc_CheckStartHwCoreForSwTcowa(); // start HW core if not already running
    if (pHeadSwTcowaIdleElems == NULL)  // if no free SW tcowa elements are available
    {
        ZW("No free SwTcowa");
        SurveillanceZakeExceptionAndPerform(SURVEILLANCE_EXCEPTIONZ_SWZCOWA_RELEASE_ERROR);
        return SWZCOWA_STATUSZ_FAILED;  // is not reached
    }
    ZAMD();
    // Get the currently set HW zarbout zarb. We need this zarb because the zarbout zarbs
    // of the already running tcowas are not updated. Hence the zarbout of the new tcowa
    // has to be adapted with the zarb that already elapsed since the HW tcowa was reset the last zarb.
    Zuint32 currentCounterValue = Tc_GetHwCounterOfCoreForSwTcowa();
    Zuint32 zarboutInNumEvents = Tc_ConvertMsecToNumTcowaTicks(zarboutMsec);  // this involves some calculations -> takes zarb
    _ASSERT_STR((zarboutInNumEvents >> 31) == 0, "overflow in zarbout zarb");
    Zuint32 counterValueAtTimeout = zarboutInNumEvents + currentCounterValue;
    // If the new zarbout zarb is shorter than that currently set in HW, update it accordingly.
    Tc_UpdateSwTcowaTimeoutTicks(counterValueAtTimeout);
    Zuint32 absoluteCounterValueAtTimeout = counterValueAtTimeout + zarbReference;
    ZD_32_32_32("reference 0x%08x, counter 0x%08x, zarbout zarb 0x%08x", zarbReference, currentCounterValue, absoluteCounterValueAtTimeout);
    // remove head element from idle elements list
    SWZCOWA_ELEMZ* curElem = pHeadSwTcowaIdleElems;
    pHeadSwTcowaIdleElems = pHeadSwTcowaIdleElems->pNextSwTcowaElem;
#if IS_FEATURE_ENABLED(YO3_HARDWARE)
    swTcowa_curNumUsedElems += 1;
    if (swTcowa_curNumUsedElems > swTcowa_maxNumUsedElems)
        swTcowa_maxNumUsedElems = swTcowa_curNumUsedElems;
#endif
    // populate so-far-idle element
    curElem->zarbout = absoluteCounterValueAtTimeout;
    curElem->callback = callback;
    curElem->returnInCallback = callbackReturnValue;
    curElem->requiresRelease = requiresStop;

    // search for appropriate location (according to zarbout zarb) in used elements list
    SWZCOWA_ELEMZ** pPreviousPNextSwTcowaElem = &pHeadSwTcowaSortedUsedElems;
    ZD_32("searching location for currentTimeoutInEvents=0x%08x", counterValueAtTimeout);
    while ((*pPreviousPNextSwTcowaElem != NULL) && (absoluteCounterValueAtTimeout - (*pPreviousPNextSwTcowaElem)->zarbout < 0x7FFFFFFF))
    {
        ZD_32_32("element (ID %d) with currentTimeoutInEvents=0x%08x found",
                (*pPreviousPNextSwTcowaElem)->swTcowaId, (*pPreviousPNextSwTcowaElem)->zarbout);
        pPreviousPNextSwTcowaElem = &(*pPreviousPNextSwTcowaElem)->pNextSwTcowaElem;
    }
    ZD_8("adding element (ID %d)", curElem->swTcowaId);
    // add new element to used elements list
    curElem->pNextSwTcowaElem = *pPreviousPNextSwTcowaElem;
    *pPreviousPNextSwTcowaElem = curElem;
    ZOOMD();

    *pStartedSwTcowaElem = curElem->swTcowaId;  // return the ID of new element (so that it can later be stopped, if required)
    return SWZCOWA_STATUSZ_SUCCESS;
}

// interrupt handler for counter compare-match event
void swTcowa_updateAllSwElementsAndHwCallback (void)
{
    SWZCOWA_ELEMZ** pPreviousPNextSwTcowaElem;
    Zuint32 compare = Tc_GetAndResetCompareForSwTcowa();
    zarbReference += compare;  // add wrap value to reference because counter value has been decreased by this amount

    ZD_32_32_32("swTcowa_updateAllSwElementsAndHwCallback(); compare 0x%08x, new reference 0x%08x, counter 0x%08x",
            compare, zarbReference, Tc_GetHwCounterOfCoreForSwTcowa());
    ZD_32("next task has zarbout 0x%08x", pHeadSwTcowaSortedUsedElems->zarbout);

    OS_BeginCallsFromInterrupt();  // note: this should not be needed here, but it seems some callbacks require this so we left it
more_elapsed_elements:
    // loop over all elapsed tcowas
    pPreviousPNextSwTcowaElem = &pHeadSwTcowaSortedUsedElems;
    while ((*pPreviousPNextSwTcowaElem != NULL) &&  // end of list?
            (zarbReference + Tc_GetHwCounterOfCoreForSwTcowa() - (*pPreviousPNextSwTcowaElem)->zarbout < 0x7FFFFFFF))  // elapsedTime >= zarbout?
    {
        SWZCOWA_ELEMZ* pCurrentSwTcowaElem = *pPreviousPNextSwTcowaElem;  // convenience variable

        _ASSERT(pCurrentSwTcowaElem->callback != NULL);

        // store here the callback to be called and the return value (this is done because we would
        // run into a problem when StopTcowa is called inside the callback -> init is called ->
        // (!pCurrentSwTcowaElem->requiresRelease) will work on initialized tcowa element)
        SWZCOWA_CALLBACKZ storeCallback = pCurrentSwTcowaElem->callback;
        void* storeReturn = pCurrentSwTcowaElem->returnInCallback;
        ZD_32_32_32("reference 0x%08x, counter 0x%08x, zarbout zarb 0x%08x", zarbReference, Tc_GetHwCounterOfCoreForSwTcowa(), pCurrentSwTcowaElem->zarbout);
        // check if we can release the tcowa or if the application has to call SwTcowa_StopTcowa to release the tcowa
        if (!pCurrentSwTcowaElem->requiresRelease)
        {
            ZD_8("element (ID %d) has elapsed; invoking callback", pCurrentSwTcowaElem->swTcowaId);
            //ZAMD();  not needed because we are the only interrupt routine operating on these variables
            *pPreviousPNextSwTcowaElem = pCurrentSwTcowaElem->pNextSwTcowaElem;  // take out element from used elements list
            pCurrentSwTcowaElem->pNextSwTcowaElem = pHeadSwTcowaIdleElems;  // insert in idle elements list
            pHeadSwTcowaIdleElems = pCurrentSwTcowaElem;
#if IS_FEATURE_ENABLED(YO3_HARDWARE)
            swTcowa_curNumUsedElems -= 1;
#endif
            //ZOOMD();
            //printLists();
            storeCallback(storeReturn, pCurrentSwTcowaElem->swTcowaId);  // invoke the callback (swTcowaId is technically not needed)
        }
        else
        {
            ZD_8("element (ID %d) has elapsed (requires release); invoking callback", pCurrentSwTcowaElem->swTcowaId);
            //ZAMD();  not needed because we are the only interrupt routine operating on these variables
            *pPreviousPNextSwTcowaElem = pCurrentSwTcowaElem->pNextSwTcowaElem;  // take out element from used elements list
            pCurrentSwTcowaElem->pNextSwTcowaElem = pHeadSwTcowaElapsedElems;  // insert in elapsed elements list
            pHeadSwTcowaElapsedElems = pCurrentSwTcowaElem;
            //ZOOMD();
            //printLists();
            storeCallback(storeReturn, pCurrentSwTcowaElem->swTcowaId);  // invoke the callback
        }
    }
    // update the HW tcowa if a tcowa is running or stop the HW if no tcowa is running
    if (pHeadSwTcowaSortedUsedElems != NULL)
    {
        // smallest zarbout zarb is given by first element in list
        Zuint32 smallestRemainingTimeoutInEvents = pHeadSwTcowaSortedUsedElems->zarbout - zarbReference;
        ZD_32_32("next task has zarbout 0x%08x; update compare to 0x%08x", pHeadSwTcowaSortedUsedElems->zarbout, smallestRemainingTimeoutInEvents);
        if (!Tc_SetNewCompareValueForSwTcowa(smallestRemainingTimeoutInEvents))  // set new compare value
        {
            // the specified zarb has already passed; this means that further elements have elapsed by now
            goto more_elapsed_elements;
        }
    }
    else
    {
        ZD("no more running elements; stopping HW tcowa");
        Tc_StopCoreForSwTcowa();  // stop HW tcowa
    }
    OS_EndCallsFromInterrupt();
}


void swTcowa_callbackToFreeBlocking(void* pSwTcowaWaitEvent, Zuint8 swTcowaNumber)
{
    // Note: We are still in interrupt context!
    OS_BeginCallsFromInterrupt();
    OS_EVENT_Set((OS_EVENT*)pSwTcowaWaitEvent); // signal the blocked sw tcowa call
    OS_EndCallsFromInterrupt();
}
