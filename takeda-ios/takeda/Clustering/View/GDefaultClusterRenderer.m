#import <CoreText/CoreText.h>
#import "GDefaultClusterRenderer.h"
#import "GQuadItem.h"
#import "GCluster.h"


#define countMore100Color RGB(55,70,142)
#define countMore10Color RGB(62,124,217)
#define countLess10Color RGB(71,162,178)


@implementation GDefaultClusterRenderer {
    GMSMapView *_map;
    NSMutableArray *_markerCache;
    GMSMarker *oldSelectedMarker;
}

- (id)initWithMapView:(GMSMapView*)googleMap {
    if (self = [super init]) {
        _map = googleMap;
        _markerCache = [[NSMutableArray alloc] init];
    }
    return self;
}

- (void)clustersChanged:(NSSet*)clusters {
    oldSelectedMarker = _map.selectedMarker;
    GMSMarker *freshSelectedMarker;
    
    for (GMSMarker *marker in _markerCache) {
        marker.map = nil;
    }
    
    [_markerCache removeAllObjects];
    
    for (id <GCluster> cluster in clusters) {
        
        GMSMarker *marker;
        marker = [[GMSMarker alloc] init];
        [_markerCache addObject:marker];
        
        NSUInteger count = cluster.items.count;
        if (count > 1) {
            marker.icon = [self generateClusterIconWithCount:count];
            marker.userData = @{@"clusterItems":cluster.items.allObjects};
            BOOL contains = NO;
            if (oldSelectedMarker!=nil){
                contains = [self clusterContainsMarker:cluster];
            }
            
            if (contains){
                freshSelectedMarker = marker;
                freshSelectedMarker.userData = oldSelectedMarker.userData;
            }
            
            NSLog(@"cluster contain = %i",contains);
//            if (oldSelectedMarker!=nil){
//                NSLog(@"items = %@",cluster.items.allObjects);
//                for (int i = 0; i< cluster.items.allObjects.count; i++){
//                    GQuadItem *iitem = cluster.items.allObjects[i];
//                    if ([oldSelectedMarker.userData[@"id"] intValue] == [iitem.marker.userData[@"id"] intValue]){
//                        freshSelectedMarker = marker;
//                    }
//                }
//            }
            
        }
        else {
            marker.icon = cluster.marker.icon;
            marker.userData = cluster.marker.userData;
            if ([oldSelectedMarker.userData[@"id"] intValue] ==[marker.userData[@"id"] intValue]){
                
                //            if (oldSelectedMarker.position.latitude == marker.position.latitude&&oldSelectedMarker.position.longitude == marker.position.longitude&&[oldSelectedMarker.userData[@"id"] intValue] ==[marker.userData[@"id"] intValue]){
                freshSelectedMarker = marker;
            }
            
        }
        
        marker.position = cluster.marker.position;
        marker.map = _map;
        
        
    }
    if (freshSelectedMarker){
        [_map setSelectedMarker:freshSelectedMarker];
    }
    
}

-(BOOL)clusterContainsMarker:(id <GCluster>)cluster{
    for (int i = 0; i< cluster.items.allObjects.count; i++){
        GQuadItem *iitem = cluster.items.allObjects[i];
        if ([self itemContainsMarker:iitem.items.allObjects]==YES){
            return YES;
        };
    }
    return NO;
}

-(BOOL)itemContainsMarker:(NSArray*)iitems{
    NSUInteger count = iitems.count;
    if (count > 1) {
        for (int i = 0; i< iitems.count; i++){
            if ([self itemContainsMarker:iitems]==YES){
                return YES;
            }
        }
        
    } else {
        GQuadItem *iitem = iitems[0];
        if ([oldSelectedMarker.userData[@"id"] intValue] == [iitem.marker.userData[@"id"] intValue]){
            return YES;
        } else {
            return NO;
        }
    }
    
    return NO;
}

- (UIImage*)generateClusterIconWithCount:(NSUInteger)count {
    
    int diameter = 40;
    float inset = 2;
    
    CGRect rect = CGRectMake(0, 0, diameter, diameter);
    UIGraphicsBeginImageContextWithOptions(rect.size, NO, 0);

    CGContextRef ctx = UIGraphicsGetCurrentContext();

    // set stroking color and draw circle
    [[UIColor colorWithRed:1 green:1 blue:1 alpha:0.8] setStroke];
    
    if (count > 100) [countMore100Color setFill];
    else if (count > 10) [countMore10Color setFill];
    else [countLess10Color setFill];

    CGContextSetLineWidth(ctx, inset);

    // make circle rect 5 px from border
    CGRect circleRect = CGRectMake(0, 0, diameter, diameter);
    circleRect = CGRectInset(circleRect, inset, inset);

    // draw circle
    CGContextFillEllipseInRect(ctx, circleRect);
    CGContextStrokeEllipseInRect(ctx, circleRect);

    CTFontRef myFont = CTFontCreateWithName( (CFStringRef)@"Helvetica-Bold", 13.0f, NULL);
    
    UIColor *fontColor = [UIColor whiteColor];
//    if ((count < 100) && count > 10) fontColor = [UIColor blackColor];
//    else fontColor = [UIColor whiteColor];
    
    NSDictionary *attributesDict = [NSDictionary dictionaryWithObjectsAndKeys:
            (__bridge id)myFont, (id)kCTFontAttributeName,
                    fontColor, (id)kCTForegroundColorAttributeName, nil];

    // create a naked string
    NSString *string = [[NSString alloc] initWithFormat:@"%lu", (unsigned long)count];

    NSAttributedString *stringToDraw = [[NSAttributedString alloc] initWithString:string
                                                                       attributes:attributesDict];

    // flip the coordinate system
    CGContextSetTextMatrix(ctx, CGAffineTransformIdentity);
    CGContextTranslateCTM(ctx, 0, diameter);
    CGContextScaleCTM(ctx, 1.0, -1.0);

    CTFramesetterRef frameSetter = CTFramesetterCreateWithAttributedString((__bridge CFAttributedStringRef)(stringToDraw));
    CGSize suggestedSize = CTFramesetterSuggestFrameSizeWithConstraints(
                                                                        frameSetter, /* Framesetter */
                                                                        CFRangeMake(0, stringToDraw.length), /* String range (entire string) */
                                                                        NULL, /* Frame attributes */
                                                                        CGSizeMake(diameter, diameter), /* Constraints (CGFLOAT_MAX indicates unconstrained) */
                                                                        NULL /* Gives the range of string that fits into the constraints, doesn't matter in your situation */
                                                                        );
    CFRelease(frameSetter);
    
    //Get the position on the y axis
    float midHeight = diameter;
    midHeight -= suggestedSize.height;
    
    float midWidth = diameter / 2;
    midWidth -= suggestedSize.width / 2;

    CTLineRef line = CTLineCreateWithAttributedString(
            (__bridge CFAttributedStringRef)stringToDraw);
    CGContextSetTextPosition(ctx, midWidth, 15);
    CTLineDraw(line, ctx);

    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    return image;
}

@end
