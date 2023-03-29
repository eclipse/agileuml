string StringLib::format(string f, vector<void*>* sq)
{
    int n = sq->size();
    int flength = f.length();
    if (n == 0 || flength == 0)
    {
        return f;
    }

    const char* format = f.c_str(); 
    void* ap;
    char* p = (char*) format;
    int i = 0;

    char* sval;
    int ival;
    unsigned int uval;
    double dval;

    ostringstream buff; 
    ap = sq->at(0);

    for (; *p != '\0'; p++)
    {
        if (*p != '%')
        {
            buff << *p;
            continue;
        }

        char* tmp = (char*)calloc(flength + 1, sizeof(char));
        tmp[0] = '%';
        int k = 1;
        p++;

        while (*p != 'i' && *p != 'd' && *p != 'g' && *p != 'e' &&
            *p != 'o' && *p != 'x' && *p != 'X' && *p != 'E' &&
            *p != 'G' && *p != 's' && *p != '%' &&
            *p != 'u' && *p != 'c' && *p != 'f' && *p != 'p')
        {
            tmp[k] = *p;
            k++;
            p++;
        }  /* Now p points to flag after % */

        tmp[k] = *p;
        tmp[k + 1] = '\0';

        if (i >= n)
        {
            continue;
        }

        switch (*p)
        {
        case 'i':
            ival = *((int*) sq->at(i));
            i++;
            { char* ibuff0 = (char*)calloc(int(log10(abs(ival)+1)) + 2, sizeof(char));
              sprintf(ibuff0, tmp, ival);
              buff << ibuff0;
            }
            break;
        case 'o':
        case 'x':
        case 'X':
        case 'u':
            uval = *((unsigned int*) sq->at(i));
            i++;
            { char* ubuff = (char*)calloc(int(log10(uval+1)) + 2, sizeof(char));
              sprintf(ubuff, tmp, uval);
              buff << ubuff;
            }
            break;
        case 'c':
        case 'd':
            ival = *((int*) sq->at(i));

            i++;
            { char* ibuff1 = (char*)calloc(int(log10(abs(ival)+1)) + 2, sizeof(char));
              sprintf(ibuff1, tmp, ival);
              buff << ibuff1;
            }
            break;
        case 'f':
        case 'e':
        case 'E':
        case 'g':
        case 'G':
            dval = *((double*) sq->at(i));
            i++;
            { char* dbuff = (char*)calloc(int(log10(fabs(dval)+1)) + 2, sizeof(char));
              sprintf(dbuff, tmp, dval);
              buff << dbuff;
            }
            break;
        case 's':
            sval = ((char*) sq->at(i));

            i++;
            { // int sn = strlen(sval) + 1;
			  // char sbuff[sn];
              // sprintf(sbuff, tmp, sval);
              buff << sval;
            }
            break;
        case 'p':
            i++;
            { char* pbuff = (char*)calloc(9, sizeof(char));
              sprintf(pbuff, tmp, sq->at(i));
              buff << pbuff;
            }
            break;
        default:
            buff << *p; 
            break;
        }
    }
    string res = buff.str(); 

    return res;
}

