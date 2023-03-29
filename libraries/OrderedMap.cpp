template< class K, class T>
T OrderedMap<K, T>::getByIndex(int i)
{
    T result = NULL;
    if (i > 0 && i <= elements->size())
    {
        result = ((T)(items)->at(((*elements)[i - 1])));
    }
    return result;
}

template< class K, class T>
T OrderedMap<K, T>::getByKey(K k)
{
    T result = NULL;
    result = ((T)(items)->at(k));
    return result;
}

template< class K, class T>
void OrderedMap<K, T>::add(K k, T t)
{
    OrderedMap* orderedmapx = this;
    elements = UmlRsdsLib<K>::concatenate(orderedmapx->getelements(), UmlRsdsLib<K>::makeSequence(k));
    items[k] = t;
}

template< class K, class T>
void OrderedMap<K, T>::remove(int i)
{
    OrderedMap* orderedmapx = this;
    K k = ((*elements)[i - 1]);

    elements = UmlRsdsLib<K>::removeAt(orderedmapx->getelements(), i);
    items = UmlRsdsOcl<K, T, T>::antirestrict(orderedmapx->getitems(), UmlRsdsLib<K>::addSet((new set<K>()), k));
}
